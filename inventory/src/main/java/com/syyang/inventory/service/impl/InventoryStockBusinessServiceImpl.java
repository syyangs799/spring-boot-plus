package com.syyang.inventory.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.syyang.inventory.entity.*;
import com.syyang.inventory.enums.ProjectOperationTypeEnum;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.enums.StockBusinessTypeEnum;
import com.syyang.inventory.mapper.InventoryProductInfoMapper;
import com.syyang.inventory.mapper.InventoryProjectOperationRecordMapper;
import com.syyang.inventory.mapper.InventoryStockBusinessMapper;
import com.syyang.inventory.mapper.InventoryStockInfoMapper;
import com.syyang.inventory.service.InventoryProductInfoService;
import com.syyang.inventory.service.InventoryProjectInfoService;
import com.syyang.inventory.service.InventoryStockBusinessService;
import com.syyang.inventory.param.InventoryStockBusinessPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syyang.springbootplus.framework.shiro.cache.LoginRedisService;
import com.syyang.springbootplus.framework.shiro.util.JwtTokenUtil;
import com.syyang.springbootplus.framework.shiro.util.JwtUtil;
import com.syyang.springbootplus.framework.shiro.vo.LoginSysUserRedisVo;
import com.syyang.springbootplus.framework.util.BeanUtils;
import com.syyang.springbootplus.framework.util.CommonListUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库存交易流水表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryStockBusinessServiceImpl extends BaseServiceImpl<InventoryStockBusinessMapper, InventoryStockBusiness> implements InventoryStockBusinessService {

    @Autowired
    private InventoryStockBusinessMapper inventoryStockBusinessMapper;
    @Autowired
    private InventoryStockInfoMapper inventoryStockInfoMapper;
    @Autowired
    private InventoryProductInfoMapper inventoryProductInfoMapper;
    @Autowired
    private InventoryProjectInfoService inventoryProjectInfoService;
    @Autowired
    private InventoryProjectOperationRecordMapper inventoryProjectOperationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryStockBusiness(InventoryStockBusiness inventoryStockBusiness) throws Exception {
        //修改总金额
        inventoryStockBusiness.setProductAmount(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductPrice()))
                .multiply(BigDecimal.valueOf(inventoryStockBusiness.getProductNum())).toString());
        boolean result = false;
        boolean save = super.save(inventoryStockBusiness);
        //添加库存信息的逻辑
        if (save) {
            InventoryStockInfo inventoryStockInfo = new InventoryStockInfo();
            //查询当前库存表是否有相应的产品信息
            LambdaQueryWrapper<InventoryStockInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InventoryStockInfo::getProductId, inventoryStockBusiness.getProductId());
            List<InventoryStockInfo> inventoryStockInfos = inventoryStockInfoMapper.selectList(wrapper);
            if (inventoryStockInfos.size() == 0) {
                inventoryStockInfo.setProductId(inventoryStockBusiness.getProductId());
                if (StockBusinessTypeEnum.IN.getCode().equals(Integer.valueOf(inventoryStockBusiness.getType()))) {
                    inventoryStockInfo.setProductAmount(inventoryStockBusiness.getProductAmount());
                    inventoryStockInfo.setProductNum(inventoryStockBusiness.getProductNum());
                } else {
                    //出库
                    inventoryStockInfo.setProductAmount(BigDecimal.valueOf(0L).subtract(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductAmount()))).toString());
                    inventoryStockInfo.setProductNum(0 - inventoryStockBusiness.getProductNum());
                }
                //添加产品名称
                LambdaQueryWrapper<InventoryProductInfo> productInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                productInfoLambdaQueryWrapper.eq(InventoryProductInfo::getId, inventoryStockBusiness.getProductId());
                InventoryProductInfo inventoryProductInfo = inventoryProductInfoMapper.selectOne(productInfoLambdaQueryWrapper);
                if (null == inventoryProductInfo) {
                    throw new Exception("当前流水新增出错，请联系管理员！！");
                }
                inventoryStockInfo.setProductName(inventoryProductInfo.getProductName());
                inventoryStockInfo.setProductType(inventoryProductInfo.getProductType());
                inventoryStockInfo.setProductModel(inventoryProductInfo.getProductModel());
                inventoryStockInfo.setProductParam(inventoryProductInfo.getProductParam());
                inventoryStockInfo.setProductStandAmount(inventoryProductInfo.getProductStandAmount());
                inventoryStockInfo.setProductManufactor(inventoryProductInfo.getProductManufactor());
                result = inventoryStockInfoMapper.insert(inventoryStockInfo) > 0;
            } else if (inventoryStockInfos.size() == 1) {
                inventoryStockInfo = inventoryStockInfos.get(0);
                if (StockBusinessTypeEnum.IN.getCode().equals(Integer.valueOf(inventoryStockBusiness.getType()))) {
                    inventoryStockInfo.setProductAmount(BigDecimal.valueOf(Double.valueOf(inventoryStockInfo.getProductAmount())).add(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductAmount()))).toString());
                    inventoryStockInfo.setProductNum(inventoryStockInfo.getProductNum() + inventoryStockBusiness.getProductNum());
                } else {
                    inventoryStockInfo.setProductAmount(BigDecimal.valueOf(Double.valueOf(inventoryStockInfo.getProductAmount())).subtract(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductAmount()))).toString());
                    inventoryStockInfo.setProductNum(inventoryStockInfo.getProductNum() - inventoryStockBusiness.getProductNum());
                }
                result = inventoryStockInfoMapper.updateById(inventoryStockInfo) > 0;
            } else {
                //删除
                log.error("当前库存有误，存在多个相同产品的库存信息，故删除重新计算");
                List<Integer> ids = Lists.newArrayList();
                for(InventoryStockInfo idInvemtory:inventoryStockInfos){
                    ids.add(idInvemtory.getId());
                }
                inventoryStockInfoMapper.deleteBatchIds(ids);
                //重新计算库存数量
                //查询所有的当前库存流水信息
                LambdaQueryWrapper<InventoryStockBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                businessLambdaQueryWrapper.eq(InventoryStockBusiness::getProductId, inventoryStockBusiness.getProductId());
                List<InventoryStockBusiness> inventoryStockBusinesses = inventoryStockBusinessMapper.selectList(businessLambdaQueryWrapper);
                BigDecimal amount = new BigDecimal("0");
                int productNum = 0;
                for (InventoryStockBusiness inventory : inventoryStockBusinesses) {
                    if (StockBusinessTypeEnum.IN.getCode().equals(Integer.valueOf(inventory.getType()))) {
                        amount = amount.add(BigDecimal.valueOf(Double.valueOf(inventory.getProductAmount())));
                        productNum = productNum + inventory.getProductNum();
                    } else {
                        amount = amount.subtract(BigDecimal.valueOf(Double.valueOf(inventory.getProductAmount())));
                        productNum = productNum - inventory.getProductNum();
                    }
                }
                inventoryStockInfo.setProductId(inventoryStockBusiness.getProductId());
                inventoryStockInfo.setProductAmount(amount.toString());
                inventoryStockInfo.setProductNum(productNum);
                //添加产品名称
                LambdaQueryWrapper<InventoryProductInfo> productInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                productInfoLambdaQueryWrapper.eq(InventoryProductInfo::getId, inventoryStockBusiness.getProductId());
                InventoryProductInfo inventoryProductInfo = inventoryProductInfoMapper.selectOne(productInfoLambdaQueryWrapper);
                if (null == inventoryProductInfo) {
                    throw new Exception("当前流水新增出错，请联系管理员！！");
                }
                inventoryStockInfo.setProductName(inventoryProductInfo.getProductName());
                inventoryStockInfo.setProductType(inventoryProductInfo.getProductType());
                inventoryStockInfo.setProductModel(inventoryProductInfo.getProductModel());
                inventoryStockInfo.setProductParam(inventoryProductInfo.getProductParam());
                inventoryStockInfo.setProductStandAmount(inventoryProductInfo.getProductStandAmount());
                inventoryStockInfo.setProductManufactor(inventoryProductInfo.getProductManufactor());
                result = inventoryStockInfoMapper.insert(inventoryStockInfo) > 0;
            }
            if (!result) {
                throw new Exception("当前流水新增出错，请联系管理员！！");
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryStockBusiness(InventoryStockBusiness inventoryStockBusiness) throws Exception {
        boolean b = super.updateById(inventoryStockBusiness);
        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryStockBusiness(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryStockBusiness> getInventoryStockBusinessPageList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam) throws Exception {
        Page<InventoryStockBusiness> page = new PageInfo<>(inventoryStockBusinessPageParam, OrderItem.desc(getLambdaColumn(InventoryStockBusiness::getCreateTime)));
        LambdaQueryWrapper<InventoryStockBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryStockBusinessPageParam.getProductId()),InventoryStockBusiness::getProductId,inventoryStockBusinessPageParam.getProductId());
        wrapper.eq(null != inventoryStockBusinessPageParam.getProjectId(),InventoryStockBusiness::getProjectId,inventoryStockBusinessPageParam.getProjectId());
        wrapper.eq(null != inventoryStockBusinessPageParam.getType(),InventoryStockBusiness::getType,inventoryStockBusinessPageParam.getType());
        IPage<InventoryStockBusiness> iPage = inventoryStockBusinessMapper.selectPage(page, wrapper);
        return new Paging<InventoryStockBusiness>(iPage);
    }

    @Override
    public boolean addOutStockBusiness(List<InventoryStockBusiness> inventoryStockBusiness) throws Exception {
        if(inventoryStockBusiness.size()<0){
            throw new Exception("当前出库记录新增出错，请选择相应的数据！！");
        }
        boolean result = false;
        String proid = inventoryStockBusiness.get(0).getProductId();
        //计算出库金额
        for(InventoryStockBusiness inventoryStockBusiness1:inventoryStockBusiness){
            //修改出库总金额
            inventoryStockBusiness1.setProductAmount(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness1.getProductPrice()))
                    .multiply(BigDecimal.valueOf(inventoryStockBusiness1.getProductNum())).toString());
        }
        boolean save = super.saveBatch(inventoryStockBusiness);
        if(save) {
            //重新计算库存信息
            LambdaQueryWrapper<InventoryStockInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InventoryStockInfo::getProductId, proid);
            List<InventoryStockInfo> inventoryStockInfos = inventoryStockInfoMapper.selectList(wrapper);
            log.error("当前库存有误，存在多个相同产品的库存信息，故删除重新计算");
            List<Integer> ids = Lists.newArrayList();
            for(InventoryStockInfo idInvemtory:inventoryStockInfos){
                ids.add(idInvemtory.getId());
            }
            inventoryStockInfoMapper.deleteBatchIds(ids);
            //重新计算库存数量
            //查询所有的当前库存流水信息
            LambdaQueryWrapper<InventoryStockBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            businessLambdaQueryWrapper.eq(InventoryStockBusiness::getProductId, proid);
            List<InventoryStockBusiness> inventoryStockBusinesses = inventoryStockBusinessMapper.selectList(businessLambdaQueryWrapper);
            BigDecimal amount = new BigDecimal("0");
            int productNum = 0;
            for (InventoryStockBusiness inventory : inventoryStockBusinesses) {
                if (StockBusinessTypeEnum.IN.getCode().equals(Integer.valueOf(inventory.getType()))) {
                    amount = amount.add(BigDecimal.valueOf(Double.valueOf(inventory.getProductAmount())));
                    productNum = productNum + inventory.getProductNum();
                } else {
                    amount = amount.subtract(BigDecimal.valueOf(Double.valueOf(inventory.getProductAmount())));
                    productNum = productNum - inventory.getProductNum();
                }
            }
            InventoryStockInfo inventoryStockInfo = new InventoryStockInfo();
            inventoryStockInfo.setProductId(proid);
            inventoryStockInfo.setProductAmount(amount.toString());
            inventoryStockInfo.setProductNum(productNum);
            //添加产品名称
            LambdaQueryWrapper<InventoryProductInfo> productInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            productInfoLambdaQueryWrapper.eq(InventoryProductInfo::getId, proid);
            InventoryProductInfo inventoryProductInfo = inventoryProductInfoMapper.selectOne(productInfoLambdaQueryWrapper);
            if (null == inventoryProductInfo) {
                throw new Exception("当前流水新增出错，请联系管理员！！");
            }
            inventoryStockInfo.setProductName(inventoryProductInfo.getProductName());
            inventoryStockInfo.setProductType(inventoryProductInfo.getProductType());
            inventoryStockInfo.setProductModel(inventoryProductInfo.getProductModel());
            inventoryStockInfo.setProductParam(inventoryProductInfo.getProductParam());
            inventoryStockInfo.setProductStandAmount(inventoryProductInfo.getProductStandAmount());
            inventoryStockInfo.setProductManufactor(inventoryProductInfo.getProductManufactor());
            result = inventoryStockInfoMapper.insert(inventoryStockInfo) > 0;
            if (!result) {
                throw new Exception("当前流水新增出错，请联系管理员！！");
            }
        }
        for(InventoryStockBusiness entity:inventoryStockBusiness){
            //新增流水之后添加项目的计算和变动日志
            inventoryProjectInfoService.calculateProjectInformation(entity.getProjectId());
            //添加项目操作日志 谁创建了项目
            InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
            inventoryProjectOperationRecord.setProjectId(entity.getProjectId());
            //谁+时间+操作类型+内容
            inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) +  "],时间[" + DateTime.now().toString("yyyy-MM-DD HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_CREATE_OUTBOUND_INFO.getDesc() + "]");
            inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_CREATE_OUTBOUND_INFO.getDesc());
            inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_CREATE_OUTBOUND_INFO.getCode().toString());
            inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(new InventoryStockBusiness(),entity));
            inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        }
        return result;
    }

    @Override
    public List<InventoryStockBusiness> getInventoryStockBusinessList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam) {
        LambdaQueryWrapper<InventoryStockBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryStockBusinessPageParam.getProductId()),InventoryStockBusiness::getProductId,inventoryStockBusinessPageParam.getProductId());
        wrapper.eq(null != inventoryStockBusinessPageParam.getProjectId(),InventoryStockBusiness::getProjectId,inventoryStockBusinessPageParam.getProjectId());
        wrapper.eq(null != inventoryStockBusinessPageParam.getType(),InventoryStockBusiness::getType,inventoryStockBusinessPageParam.getType());
        return inventoryStockBusinessMapper.selectList(wrapper);
    }

    @Override
    public List<InventoryStockBusiness> getInventoryStockBusinessDeliveredList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam) {
        LambdaQueryWrapper<InventoryStockBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStockBusiness::getProductId,inventoryStockBusinessPageParam.getProductId());
        List<InventoryStockBusiness> inventoryStockBusinesses = inventoryStockBusinessMapper.selectList(wrapper);
        Map<Integer, List<InventoryStockBusiness>> proInventoryStockBusiness = new LinkedHashMap<Integer, List<InventoryStockBusiness>>();
        CommonListUtils.listGroup2Map(inventoryStockBusinesses, proInventoryStockBusiness, InventoryStockBusiness.class, "getType");// 输入方法名

        //入库
        List<InventoryStockBusiness> in = proInventoryStockBusiness.getOrDefault(StockBusinessTypeEnum.IN.getCode(),Lists.newArrayList());
        //出库
        List<InventoryStockBusiness> out = proInventoryStockBusiness.getOrDefault(StockBusinessTypeEnum.OUT.getCode(),Lists.newArrayList());
        Map<Integer, List<InventoryStockBusiness>> outMap = new LinkedHashMap<Integer, List<InventoryStockBusiness>>();
        CommonListUtils.listGroup2Map(out, outMap, InventoryStockBusiness.class, "getBachId");// 输入方法名

        List<InventoryStockBusiness> newIn = Lists.newArrayList();
        //计算 减去已经出库的
        for(InventoryStockBusiness now:in){
            if(outMap.containsKey(now.getId())){
                BigDecimal amount = BigDecimal.valueOf(Double.valueOf(now.getProductAmount()));
                int productNum = now.getProductNum();
                for (InventoryStockBusiness entryOut : outMap.get(now.getId())) {
                    amount = amount.subtract(BigDecimal.valueOf(Double.valueOf(entryOut.getProductAmount())));
                    productNum = productNum - entryOut.getProductNum();
                }
                now.setProductAmount(amount.toString());
                now.setProductNum(productNum);
            }
            if(now.getProductNum()>0){
                //有剩余就返回
                newIn.add(now);
            }
        }
        return newIn;
    }

}
