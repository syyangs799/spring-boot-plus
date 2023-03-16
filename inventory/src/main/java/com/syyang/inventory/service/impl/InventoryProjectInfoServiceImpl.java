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
        boolean save = super.save(inventoryProjectInfo);
        calculateProjectInformation(inventoryProjectInfo.getId());
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception {
        boolean b = super.updateById(inventoryProjectInfo);
        calculateProjectInformation(inventoryProjectInfo.getId());
        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProjectInfo(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProjectInfo> getInventoryProjectInfoPageList(InventoryProjectInfoPageParam inventoryProjectInfoPageParam) {
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
        log.info("开始计算项目的相关金额，proId:{}",proId);
        InventoryProjectInfo inventoryProjectInfo = inventoryProjectInfoMapper.selectById(proId);
        //查询项目的收支数据
        LambdaQueryWrapper<InventoryProjectBusiness> inventoryProjectBusinessLambdaQueryWrapper= new LambdaQueryWrapper<>();
        inventoryProjectBusinessLambdaQueryWrapper.eq(InventoryProjectBusiness::getProId,inventoryProjectInfo.getId());
        List<InventoryProjectBusiness> inventoryProjectBusinesses = inventoryProjectBusinessMapper.selectList(inventoryProjectBusinessLambdaQueryWrapper);

        Map<Integer, List<InventoryProjectBusiness>> map = new LinkedHashMap<Integer, List<InventoryProjectBusiness>>();
        CommonListUtils.listGroup2Map(inventoryProjectBusinesses, map, InventoryProjectBusiness.class, "getType");// 输入方法名
        //统计项目基本信息
        log.info("开始计算项目的基本金额，proId:{}",proId);
        calculateProjectForBaseInfo(inventoryProjectInfo,map);
        //计算项目的 统计信息
        log.info("开始计算项目的统计金额，proId:{}",proId);
        calculateProjectForTotalMoney(inventoryProjectInfo,map);
        log.info("计算项目的相关金额完毕，更新项目信息，proId:{}",proId);
        return inventoryProjectInfoMapper.updateById(inventoryProjectInfo) > 0;
    }

    /**
     * 计算基础信息的金额
     * @param inventoryProjectInfo
     */
    private void calculateProjectForBaseInfo(InventoryProjectInfo inventoryProjectInfo,Map<Integer, List<InventoryProjectBusiness>> map) {
        //判断项目金额和质保金 如果无 置为0
        //合同金额
        if(null == inventoryProjectInfo.getAmountContract()){
            inventoryProjectInfo.setAmountContract("0");
        }
        //商务费用
        if(null == inventoryProjectInfo.getAmountBusiness()){
            inventoryProjectInfo.setAmountBusiness("0");
        }
        //质保金
        if(null == inventoryProjectInfo.getAmountWarranty()){
            inventoryProjectInfo.setAmountWarranty("0");
        }
        //商务费用 手动输入
//        inventoryProjectInfo.setAmountBusiness();
        BigDecimal costIncludingTax = new BigDecimal(0);
        BigDecimal costExcludingTax = new BigDecimal(0);
        //支出记录
        for(InventoryProjectBusiness inventoryProjectBusiness:map.getOrDefault(1, Lists.newArrayList())){
            costIncludingTax = costIncludingTax.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
            //先加价格 再减去税
            costExcludingTax = costExcludingTax.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())))
                    .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountTaxes())));
        }
        //计算含税成本
        inventoryProjectInfo.setAmountCostTax(costIncludingTax.toString());
        //计算不含税成本
        inventoryProjectInfo.setAmountCostNoTax(costExcludingTax.toString());
        //计算税务成本 (合同金额一含税成本)*15%
        inventoryProjectInfo.setAmountTax((BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountContract()))
                .subtract(costIncludingTax)).multiply(BigDecimal.valueOf(0.15)).toString());
        //计算利润 合同金额一 质保金一含税成本 不含税成本 税务成本
        inventoryProjectInfo.setAmountProfit(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountContract()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountWarranty())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCostTax())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCostNoTax())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountTax()))).toString());
        //计算业务提成 ( 利润一商务费用]*10%
        inventoryProjectInfo.setAmountCommissionBusiness(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfit()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountBusiness())))
                .multiply(BigDecimal.valueOf(0.15)).toString());
        //计算技术提成 ( 利润一商务费用]*10%
        inventoryProjectInfo.setAmountCommissionTechnical(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfit()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountBusiness())))
                .multiply(BigDecimal.valueOf(0.15)).toString());
        //计算管理提成 ( 利润一商务费用]*10%
        inventoryProjectInfo.setAmountCommissionManagement(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfit()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountBusiness())))
                .multiply(BigDecimal.valueOf(0.15)).toString());

    }

    /**
     * 重新统计项目的金额
     * @param inventoryProjectInfo
     */
    private void calculateProjectForTotalMoney(InventoryProjectInfo inventoryProjectInfo,Map<Integer, List<InventoryProjectBusiness>> map) {

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
                totalPaid = totalPaid.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
            }else if(StatusTypeEnum.CHECK_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
                totalUnpaid = totalUnpaid.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
            }
        }
        //应支付 审批通过的项目支出金额
        // 已支付 出纳确认过的项目支出金额
        // 未支付 应支付-已支付
        inventoryProjectInfo.setTotalPayable(totalUnpaid.toString());
        inventoryProjectInfo.setTotalPaid(totalPaid.toString());
        inventoryProjectInfo.setTotalUnpaid(totalUnpaid.subtract(totalPaid).toString());
    }


}
