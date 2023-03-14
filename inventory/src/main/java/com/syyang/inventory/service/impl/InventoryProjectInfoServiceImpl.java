package com.syyang.inventory.service.impl;

import com.google.common.collect.Lists;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.InventoryStockBusiness;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.mapper.InventoryProjectBusinessMapper;
import com.syyang.inventory.mapper.InventoryProjectInfoMapper;
import com.syyang.inventory.service.InventoryProjectInfoService;
import com.syyang.inventory.param.InventoryProjectInfoPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syyang.springbootplus.framework.util.CommonListUtils;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目信息表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryProjectInfoServiceImpl extends BaseServiceImpl<InventoryProjectInfoMapper, InventoryProjectInfo> implements InventoryProjectInfoService {

    @Autowired
    private InventoryProjectInfoMapper inventoryProjectInfoMapper;
    @Autowired
    private InventoryProjectBusinessMapper inventoryProjectBusinessMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception {
        return super.save(inventoryProjectInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception {
        return super.updateById(inventoryProjectInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProjectInfo(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProjectInfo> getInventoryProjectInfoPageList(InventoryProjectInfoPageParam inventoryProjectInfoPageParam) throws Exception {
        Page<InventoryProjectInfo> page = new PageInfo<>(inventoryProjectInfoPageParam, OrderItem.desc(getLambdaColumn(InventoryProjectInfo::getCreateTime)));
        LambdaQueryWrapper<InventoryProjectInfo> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryProjectInfo> iPage = inventoryProjectInfoMapper.selectPage(page, wrapper);
        return new Paging<InventoryProjectInfo>(iPage);
    }

    @Override
    public List<InventoryProjectInfo> getInventoryProjectInfoList(InventoryProjectInfoPageParam inventoryProjectInfoPageParam) {
        LambdaQueryWrapper<InventoryProjectInfo> wrapper = new LambdaQueryWrapper<>();
        return inventoryProjectInfoMapper.selectList(wrapper);
    }


    @Override
    public boolean calculateProjectInformation(Integer proId) {
        InventoryProjectInfo inventoryProjectInfo = inventoryProjectInfoMapper.selectById(proId);
        //统计项目基本信息
        calculateProjectForBaseInfo(inventoryProjectInfo);
        //计算项目的 统计信息
        calculateProjectForTotalMoney(inventoryProjectInfo);
        return inventoryProjectInfoMapper.updateById(inventoryProjectInfo) > 0;
    }

    private void calculateProjectForBaseInfo(InventoryProjectInfo inventoryProjectInfo) {
        //商务费用 手动输入
//        inventoryProjectInfo.setAmountBusiness();

    }

    /**
     * 重新统计项目的金额
     * @param inventoryProjectInfo
     */
    private void calculateProjectForTotalMoney(InventoryProjectInfo inventoryProjectInfo) {
        //查询项目的收支数据
        LambdaQueryWrapper<InventoryProjectBusiness> inventoryProjectBusinessLambdaQueryWrapper= new LambdaQueryWrapper<>();
        inventoryProjectBusinessLambdaQueryWrapper.eq(InventoryProjectBusiness::getProId,inventoryProjectInfo.getId());
        List<InventoryProjectBusiness> inventoryProjectBusinesses = inventoryProjectBusinessMapper.selectList(inventoryProjectBusinessLambdaQueryWrapper);

        Map<Integer, List<InventoryProjectBusiness>> map = new LinkedHashMap<Integer, List<InventoryProjectBusiness>>();
        CommonListUtils.listGroup2Map(inventoryProjectBusinesses, map, InventoryProjectBusiness.class, "getType");// 输入方法名


        //说明 项目应收款 = 合同金额
        //已收款 出纳确认过的项目收入的金额
        //未收 = 应收-已收
        //因收款=合同金额
        inventoryProjectInfo.setTotalReceivables(inventoryProjectInfo.getAmountContract());
        //项目统计-已收款
        BigDecimal totalReceived = new BigDecimal(0);
        //项目统计-待收款
        BigDecimal totalUnreceived = new BigDecimal(0);
        for(InventoryProjectBusiness inventoryProjectBusiness:map.getOrDefault(0, Lists.newArrayList())){
            if(StatusTypeEnum.CASHI_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
                totalReceived = totalReceived.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
            }else if(StatusTypeEnum.CHECK_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
                totalUnreceived = totalUnreceived.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
            }
        }
        inventoryProjectInfo.setTotalReceived(totalReceived.toString());
        inventoryProjectInfo.setTotalUnreceived(totalUnreceived.toString());

        //项目统计-已支付
        BigDecimal totalPaid = new BigDecimal(0);
        //项目统计-未支付
        BigDecimal totalUnpaid = new BigDecimal(0);
        for(InventoryProjectBusiness inventoryProjectBusiness:map.getOrDefault(1, Lists.newArrayList())){
            if(StatusTypeEnum.CASHI_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
                totalPaid = totalReceived.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
            }else if(StatusTypeEnum.CHECK_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
                totalUnpaid = totalUnreceived.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
            }
        }
        //应支付 审批通过的项目支出金额
        // 已支付 出纳确认过的项目支出金额
        // 未支付 应支付-已支付
        inventoryProjectInfo.setTotalPayable(totalUnpaid.toString());
        inventoryProjectInfo.setTotalPaid(totalPaid.toString());
        inventoryProjectInfo.setTotalUnpaid(totalUnpaid.divide(totalPaid).toString());
    }


}
