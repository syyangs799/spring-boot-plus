package com.syyang.inventory.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.syyang.inventory.entity.*;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.enums.ProjectOperationTypeEnum;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.enums.StepTypeEnum;
import com.syyang.inventory.enums.StockBusinessTypeEnum;
import com.syyang.inventory.mapper.InventoryProjectBusinessMapper;
import com.syyang.inventory.mapper.InventoryProjectInfoMapper;
import com.syyang.inventory.mapper.InventoryProjectOperationRecordMapper;
import com.syyang.inventory.mapper.InventoryStockBusinessMapper;
import com.syyang.inventory.service.InventoryProjectInfoService;
import com.syyang.inventory.param.InventoryProjectInfoPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syyang.springbootplus.framework.shiro.util.JwtTokenUtil;
import com.syyang.springbootplus.framework.shiro.util.JwtUtil;
import com.syyang.springbootplus.framework.util.BeanUtils;
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

    @Autowired
    private InventoryStockBusinessMapper inventoryStockBusinessMapper;
    @Autowired
    private InventoryProjectOperationRecordMapper inventoryProjectOperationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception {
        //项目状态强制置为新建
        inventoryProjectInfo.setStep(StepTypeEnum.NEW.getCode().toString());
        boolean save = super.save(inventoryProjectInfo);
        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectInfo.getId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) + "],时间[" + DateTime.now().toString("yyyy-MM-DD HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_CREATE_BASE_INFO.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_CREATE_BASE_INFO.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_CREATE_BASE_INFO.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(new InventoryProjectInfo(),inventoryProjectInfo));
        inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception {
        InventoryProjectInfo old = getById(inventoryProjectInfo.getId());
        boolean b = super.updateById(inventoryProjectInfo);
//        calculateProjectInformation(inventoryProjectInfo.getId());
        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectInfo.getId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) + "],时间[" + DateTime.now().toString("yyyy-MM-DD HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_UPDATE_BASE_INFO.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_UPDATE_BASE_INFO.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_UPDATE_BASE_INFO.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(old,inventoryProjectInfo));
        inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);

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
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectInfoPageParam.getStep()),InventoryProjectInfo::getStep,inventoryProjectInfoPageParam.getStep())
                .like(StrUtil.isNotBlank(inventoryProjectInfoPageParam.getKeyword()),InventoryProjectInfo::getProjectName,inventoryProjectInfoPageParam.getKeyword());
        IPage<InventoryProjectInfo> iPage = inventoryProjectInfoMapper.selectPage(page, wrapper);
        return new Paging<InventoryProjectInfo>(iPage);
    }

    @Override
    public List<InventoryProjectInfo> getInventoryProjectInfoList(InventoryProjectInfoPageParam inventoryProjectInfoPageParam) {
        LambdaQueryWrapper<InventoryProjectInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectInfoPageParam.getStep()),InventoryProjectInfo::getStep,inventoryProjectInfoPageParam.getStep())
                .like(StrUtil.isNotBlank(inventoryProjectInfoPageParam.getKeyword()),InventoryProjectInfo::getProjectName,inventoryProjectInfoPageParam.getKeyword());
        return inventoryProjectInfoMapper.selectList(wrapper);
    }


    @Override
    public boolean calculateProjectInformation(Integer proId) {
        log.info("开始计算项目的相关金额，proId:{}",proId);
        InventoryProjectInfo inventoryProjectInfo = inventoryProjectInfoMapper.selectById(proId);
        //判断项目金额和质保金 如果无 置为0
        //合同金额
        if(StrUtil.isEmpty(inventoryProjectInfo.getAmountContract())){
            inventoryProjectInfo.setAmountContract("0");
        }
        //商务费用
        if(StrUtil.isEmpty(inventoryProjectInfo.getAmountBusiness())){
            inventoryProjectInfo.setAmountBusiness("0");
        }
        //质保金
        if(StrUtil.isEmpty(inventoryProjectInfo.getAmountWarranty())){
            inventoryProjectInfo.setAmountWarranty("0");
        }
        //查询项目的收支数据
        LambdaQueryWrapper<InventoryProjectBusiness> inventoryProjectBusinessLambdaQueryWrapper= new LambdaQueryWrapper<>();
        inventoryProjectBusinessLambdaQueryWrapper.eq(InventoryProjectBusiness::getProId,inventoryProjectInfo.getId())
                        .and(wrapper->wrapper.eq(InventoryProjectBusiness::getStatus,StatusTypeEnum.CHECK_SUCCESS.getCode().toString())
                                .or().eq(InventoryProjectBusiness::getStatus,StatusTypeEnum.CASHI_SUCCESS.getCode().toString()));
//                .eq(InventoryProjectBusiness::getStatus,StatusTypeEnum.CASHI_SUCCESS.getCode().toString());
        List<InventoryProjectBusiness> inventoryProjectBusinesses = inventoryProjectBusinessMapper.selectList(inventoryProjectBusinessLambdaQueryWrapper);

        Map<Integer, List<InventoryProjectBusiness>> businessMap = new LinkedHashMap<Integer, List<InventoryProjectBusiness>>();
        CommonListUtils.listGroup2Map(inventoryProjectBusinesses, businessMap, InventoryProjectBusiness.class, "getType");// 输入方法名


        //查询项目的出库记录
        LambdaQueryWrapper<InventoryStockBusiness> inventoryStockBusinessLambdaQueryWrapper= new LambdaQueryWrapper<>();
        inventoryStockBusinessLambdaQueryWrapper.eq(InventoryStockBusiness::getProjectId,inventoryProjectInfo.getId())
                .eq(InventoryStockBusiness::getType,StockBusinessTypeEnum.OUT.getCode());
        List<InventoryStockBusiness> inventoryStockBusinesses = inventoryStockBusinessMapper.selectList(inventoryStockBusinessLambdaQueryWrapper);

        Map<Integer, List<InventoryStockBusiness>> stockMap = new LinkedHashMap<Integer, List<InventoryStockBusiness>>();
        CommonListUtils.listGroup2Map(inventoryStockBusinesses, stockMap, InventoryStockBusiness.class, "getType");// 输入方法名
        //统计项目基本信息
        log.info("开始计算项目的基本金额，proId:{}",proId);
        calculateProjectForBaseInfo(inventoryProjectInfo,businessMap,stockMap);
        //计算项目的 统计信息
        log.info("开始计算项目的统计金额，proId:{}",proId);
        calculateProjectForTotalMoney(inventoryProjectInfo,businessMap);
        log.info("计算项目的相关金额完毕，更新项目信息，proId:{}",proId);
        return inventoryProjectInfoMapper.updateById(inventoryProjectInfo) > 0;
    }

    @Override
    public List<KeyAndValueVo> getTotalProjectAmount(InventoryProjectInfoPageParam inventoryProjectInfoPageParam) {
        LambdaQueryWrapper<InventoryProjectInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectInfoPageParam.getStep()), InventoryProjectInfo::getStep, inventoryProjectInfoPageParam.getStep())
                .like(StrUtil.isNotBlank(inventoryProjectInfoPageParam.getKeyword()), InventoryProjectInfo::getProjectName, inventoryProjectInfoPageParam.getKeyword());
        List<InventoryProjectInfo> inventoryProjectInfos = inventoryProjectInfoMapper.selectList(wrapper);
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        BigDecimal amountContract = new BigDecimal(0);
        BigDecimal amountWarranty = new BigDecimal(0);
        BigDecimal amountProfitNet = new BigDecimal(0);
        BigDecimal totalReceivables = new BigDecimal(0);
        BigDecimal totalReceived = new BigDecimal(0);
        BigDecimal totalPayable = new BigDecimal(0);
        BigDecimal totalPaid = new BigDecimal(0);
        for (InventoryProjectInfo inventoryProjectInfo : inventoryProjectInfos) {
            amountContract = amountContract.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isEmpty(inventoryProjectInfo.getAmountContract())?"0":inventoryProjectInfo.getAmountContract())));
            amountWarranty = amountWarranty.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isEmpty(inventoryProjectInfo.getAmountWarranty())?"0":inventoryProjectInfo.getAmountWarranty())));
            amountProfitNet = amountProfitNet.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isEmpty(inventoryProjectInfo.getAmountProfitNet())?"0":inventoryProjectInfo.getAmountProfitNet())));
            totalReceivables = totalReceivables.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isEmpty(inventoryProjectInfo.getTotalReceivables())?"0":inventoryProjectInfo.getTotalReceivables())));
            totalReceived = totalReceived.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isEmpty(inventoryProjectInfo.getTotalReceived())?"0":inventoryProjectInfo.getTotalReceived())));
            totalPayable = totalPayable.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isEmpty(inventoryProjectInfo.getTotalPayable())?"0":inventoryProjectInfo.getTotalPayable())));
            totalPaid = totalPaid.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isEmpty(inventoryProjectInfo.getTotalPaid())?"0":inventoryProjectInfo.getTotalPaid())));
        }
        keyAndValueVos.add(new KeyAndValueVo("合同金额统计", amountContract.toString()));
        keyAndValueVos.add(new KeyAndValueVo("质保金统计", amountWarranty.toString()));
        keyAndValueVos.add(new KeyAndValueVo("项目纯利润统计", amountProfitNet.toString()));
        keyAndValueVos.add(new KeyAndValueVo("应收款统计", totalReceivables.toString()));
        keyAndValueVos.add(new KeyAndValueVo("已收款统计", totalReceived.toString()));
        keyAndValueVos.add(new KeyAndValueVo("应支付统计", totalPayable.toString()));
        keyAndValueVos.add(new KeyAndValueVo("已支付统计", totalPaid.toString()));
        return keyAndValueVos;
    }

    /**
     * 计算基础信息的金额
     * @param inventoryProjectInfo
     */
    private void calculateProjectForBaseInfo(InventoryProjectInfo inventoryProjectInfo,Map<Integer, List<InventoryProjectBusiness>> businessMap,Map<Integer, List<InventoryStockBusiness>> stockMap) {
        //商务费用 手动输入
//        inventoryProjectInfo.setAmountBusiness();
        BigDecimal costIncludingTax = new BigDecimal(0);
        //出库支出记录
        for(InventoryStockBusiness inventoryStockBusiness:stockMap.getOrDefault(StockBusinessTypeEnum.OUT.getCode(), Lists.newArrayList())){
            costIncludingTax = costIncludingTax.add(BigDecimal.valueOf(Double.valueOf(inventoryStockBusiness.getProductAmount())));
        }
        //计算含税成本  含税成本是指出库记录里面的成本
        inventoryProjectInfo.setAmountCostTax(costIncludingTax.toString());
        BigDecimal costExcludingTax = new BigDecimal(0);
        //项目支出记录
        for(InventoryProjectBusiness inventoryProjectBusiness:businessMap.getOrDefault(StockBusinessTypeEnum.OUT.getCode(), Lists.newArrayList())){
            if(inventoryProjectBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())) {
                //如果为出纳成功进行统计出纳金额
//                costExcludingTax = costExcludingTax.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isBlank(inventoryProjectBusiness.getCashierAmount()) ? "0" : inventoryProjectBusiness.getCashierAmount())));
                costExcludingTax = costExcludingTax.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isBlank(inventoryProjectBusiness.getAmountMoney()) ? "0" : inventoryProjectBusiness.getAmountMoney())));
            }else{
                //如果审批状态则统计支出金额
                costExcludingTax = costExcludingTax.add(BigDecimal.valueOf(Double.valueOf(StrUtil.isBlank(inventoryProjectBusiness.getAmountMoney()) ? "0" : inventoryProjectBusiness.getAmountMoney())));
            }
        }
        //计算不含税成本 不含税成本是指项目支出里面的 a及权限审批过后的金额
        inventoryProjectInfo.setAmountCostNoTax(costExcludingTax.toString());
        //计算税务成本 (合同金额一含税成本)*15%
        inventoryProjectInfo.setAmountTax((BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountContract()))
                .subtract(costIncludingTax)).multiply(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountTaxPaid()))).toString());
        //计算项目利润 合同金额一 质保金一含税成本 不含税成本 税务成本
        inventoryProjectInfo.setAmountProfit(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountContract()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountWarranty())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCostTax())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCostNoTax())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountTax()))).toString());
        //计算业务提成 ( 项目利润一商务费用]*10%
        inventoryProjectInfo.setAmountCommissionBusiness(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfit()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountBusiness())))
                .multiply(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCommissionBusinessPaid()))).toString());
        //计算技术提成 ( 项目利润一商务费用]*10%
        inventoryProjectInfo.setAmountCommissionTechnical(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfit()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountBusiness())))
                .multiply(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCommissionTechnicalPaid()))).toString());
        //计算管理提成 ( 项目利润一商务费用]*10%
        inventoryProjectInfo.setAmountCommissionManagement(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfit()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountBusiness())))
                .multiply(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCommissionManagementPaid()))).toString());
        //计算项目纯利润  项目利润- 业务提成-技术提成-管理提成 -商务费用
        inventoryProjectInfo.setAmountProfitNet(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfit()))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCommissionBusiness())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCommissionTechnical())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountCommissionManagement())))
                .subtract(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountBusiness()))).toString());
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
        for(InventoryProjectBusiness inventoryProjectBusiness:map.getOrDefault(StockBusinessTypeEnum.IN.getCode(), Lists.newArrayList())){
            if(StatusTypeEnum.CASHI_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
                totalReceived = totalReceived.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
            }
//            else if(StatusTypeEnum.CHECK_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
//                totalUnreceived = totalUnreceived.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
//            }
        }
        inventoryProjectInfo.setTotalReceived(totalReceived.toString());
        inventoryProjectInfo.setTotalUnreceived(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalReceivables())).subtract(totalReceived).toString());

        //项目统计-因支付 ==审核过的价格和出纳没关系
        BigDecimal totalPayable = new BigDecimal(0);
        //项目统计-已支付 --出纳的价格
        BigDecimal totalPaid = new BigDecimal(0);
        //项目统计-未支付  因支付-已支付
        BigDecimal totalUnpaid = new BigDecimal(0);
        for(InventoryProjectBusiness inventoryProjectBusiness:map.getOrDefault(StockBusinessTypeEnum.OUT.getCode(), Lists.newArrayList())){
            totalPayable = totalPayable.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
            if(StatusTypeEnum.CASHI_SUCCESS.getCode().equals(Integer.valueOf(inventoryProjectBusiness.getStatus()))){
                totalPaid = totalPaid.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
            }
        }
        //应支付 审批通过的项目支出金额
        // 已支付 出纳确认过的项目支出金额
        // 未支付 应支付-已支付
        totalUnpaid = totalPayable.subtract(totalPaid);
        inventoryProjectInfo.setTotalPayable(totalPayable.toString());
        inventoryProjectInfo.setTotalPaid(totalPaid.toString());
        inventoryProjectInfo.setTotalUnpaid(totalUnpaid.toString());
    }


}
