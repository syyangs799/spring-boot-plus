package com.syyang.inventory.service.impl;

import com.google.common.collect.Lists;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.entity.InventoryStockBusiness;
import com.syyang.inventory.entity.InventoryStockInfo;
import com.syyang.inventory.enums.StockBusinessTypeEnum;
import com.syyang.inventory.mapper.InventoryProductInfoMapper;
import com.syyang.inventory.mapper.InventoryStockBusinessMapper;
import com.syyang.inventory.mapper.InventoryStockInfoMapper;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryStockBusiness(InventoryStockBusiness inventoryStockBusiness) throws Exception {
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
                    inventoryStockInfo.setProductAmount(BigDecimal.valueOf(0L).divide(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductAmount()))).toString());
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
                result = inventoryStockInfoMapper.insert(inventoryStockInfo) > 0;
            } else if (inventoryStockInfos.size() == 1) {
                inventoryStockInfo = inventoryStockInfos.get(0);
                if (StockBusinessTypeEnum.IN.getCode().equals(Integer.valueOf(inventoryStockBusiness.getType()))) {
                    inventoryStockInfo.setProductAmount(BigDecimal.valueOf(Double.valueOf(inventoryStockInfo.getProductAmount())).add(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductAmount()))).toString());
                    inventoryStockInfo.setProductNum(inventoryStockInfo.getProductNum() + inventoryStockBusiness.getProductNum());
                } else {
                    inventoryStockInfo.setProductAmount(BigDecimal.valueOf(Double.valueOf(inventoryStockInfo.getProductAmount())).divide(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductAmount()))).toString());
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
                        amount = amount.divide(BigDecimal.valueOf(Double.valueOf(inventory.getProductAmount())));
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
        return super.updateById(inventoryStockBusiness);
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
        wrapper.eq(InventoryStockBusiness::getProductId,inventoryStockBusinessPageParam.getProductId());
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
                    amount = amount.divide(BigDecimal.valueOf(Double.valueOf(inventory.getProductAmount())));
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
            result = inventoryStockInfoMapper.insert(inventoryStockInfo) > 0;
            if (!result) {
                throw new Exception("当前流水新增出错，请联系管理员！！");
            }
        }
        return result;
    }

    @Override
    public List<InventoryStockBusiness> getInventoryStockBusinessList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam) {
        LambdaQueryWrapper<InventoryStockBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStockBusiness::getProductId,inventoryStockBusinessPageParam.getProductId());
        return inventoryStockBusinessMapper.selectList(wrapper);
    }

    @Override
    public List<InventoryStockBusiness> getInventoryStockBusinessDeliveredList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam) {
        LambdaQueryWrapper<InventoryStockBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStockBusiness::getProductId,inventoryStockBusinessPageParam.getProductId());
        List<InventoryStockBusiness> inventoryStockBusinesses = inventoryStockBusinessMapper.selectList(wrapper);
        Map<String, List<InventoryStockBusiness>> proInventoryStockBusiness = new LinkedHashMap<String, List<InventoryStockBusiness>>();
        CommonListUtils.listGroup2Map(inventoryStockBusinesses, proInventoryStockBusiness, InventoryStockBusiness.class, "getType");// 输入方法名

        //入库
        List<InventoryStockBusiness> in = proInventoryStockBusiness.get("1");
        //出库
        List<InventoryStockBusiness> out = proInventoryStockBusiness.get("2");
        Map<Integer, List<InventoryStockBusiness>> outMap = new LinkedHashMap<Integer, List<InventoryStockBusiness>>();
        CommonListUtils.listGroup2Map(out, outMap, InventoryStockBusiness.class, "getBachId");// 输入方法名

        List<InventoryStockBusiness> newIn = Lists.newArrayList();
        //计算 减去已经出库的
        for(InventoryStockBusiness now:in){
            if(outMap.containsKey(now.getId())){
                BigDecimal amount = BigDecimal.valueOf(Double.valueOf(now.getProductAmount()));
                int productNum = now.getProductNum();
                for (InventoryStockBusiness entryOut : outMap.get(now.getId())) {
                    amount = amount.divide(BigDecimal.valueOf(Double.valueOf(entryOut.getProductAmount())));
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
