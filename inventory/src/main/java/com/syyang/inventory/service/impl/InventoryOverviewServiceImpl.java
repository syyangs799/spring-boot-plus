package com.syyang.inventory.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.syyang.inventory.entity.*;
import com.syyang.inventory.entity.vo.*;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.enums.StepTypeEnum;
import com.syyang.inventory.enums.StockBusinessTypeEnum;
import com.syyang.inventory.mapper.*;
import com.syyang.inventory.param.InventoryOverviewParam;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
import com.syyang.inventory.service.InventoryOverviewService;
import com.syyang.inventory.service.InventoryProductInfoService;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.shiro.cache.LoginRedisService;
import com.syyang.springbootplus.framework.shiro.util.JwtTokenUtil;
import com.syyang.springbootplus.framework.shiro.util.JwtUtil;
import com.syyang.springbootplus.framework.shiro.vo.LoginSysUserRedisVo;
import com.syyang.springbootplus.framework.util.CommonListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 产品信息表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryOverviewServiceImpl extends BaseServiceImpl<InventoryProductInfoMapper, InventoryProductInfo> implements InventoryOverviewService {

    @Autowired
    private InventoryDailyTypeMapper inventoryDailyTypeMapper;
    @Autowired
    private InventoryDailyBusinessMapper inventoryDailyBusinessMapper;
    @Autowired
    private InventoryProjectInfoMapper inventoryProjectInfoMapper;
    @Autowired
    private InventoryProjectBusinessMapper inventoryProjectBusinessMapper;
    @Autowired
    private InventoryProjectOtherBusinessMapper inventoryProjectOtherBusinessMapper;
    @Autowired
    private InventoryStockAgreementMapper inventoryStockAgreementMapper;
    @Autowired
    private LoginRedisService loginRedisService;

    @Override
    public List<KeyAndValue2Vo> getProjectFinance(InventoryOverviewParam inventoryOverviewParam) {
        //判断一下当前时间是否为null，默认填充最近7天的日期
        isNullForInventoryOverviewParam(inventoryOverviewParam);
        List<KeyAndValue2Vo> keyAndValueVos = Lists.newArrayList();
        List<InventoryProjectInfo> inventoryProjectInfos = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,false,false);
        BigDecimal yue = new BigDecimal(0);
        BigDecimal yingshou = new BigDecimal(0);
        BigDecimal yishou = new BigDecimal(0);
        BigDecimal zhibao = new BigDecimal(0);
        BigDecimal yinfu = new BigDecimal(0);
        BigDecimal yifu = new BigDecimal(0);
        List<KeyAndValueVo> yingshouDatas = Lists.newArrayList();
        List<KeyAndValueVo> yishouDatas = Lists.newArrayList();
        List<KeyAndValueVo> zhibaoDatas = Lists.newArrayList();
        List<KeyAndValueVo> yinfuDatas = Lists.newArrayList();
        List<KeyAndValueVo> yifuDatas = Lists.newArrayList();
        BigDecimal weifu = new BigDecimal(0);
        BigDecimal yingdelirun = new BigDecimal(0);
//        总余额，时间范围内 出纳的收入总金额和支出总金额 项目收支 日常 合同和项目提成
//        应收总余额 时间范围内 项目的应收总金额  项目创建时间
//        已收总金额 时间范围内 出纳的收入总金额
//        质保金 时间范围内 完结的项目的质保金
//        应付总金额 时间范围内 审核过后的金额 项目支出 日常支出 采购合同 项目提成 金额
//        已付 时间范围内 出纳的支出总金额 项目收支 日常 合同和项目提成
//        未支付 应付-已付
        for(InventoryProjectInfo inventoryProjectInfo:inventoryProjectInfos){
            yingshou = yingshou.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalReceivables())));
            yingshouDatas.add(new KeyAndValueVo("[项目]"+inventoryProjectInfo.getProjectName(),inventoryProjectInfo.getTotalReceivables()));
//            yishou = yishou.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalReceived())));
            if(inventoryProjectInfo.getStep().equals(StepTypeEnum.FINISHED.getCode().toString())) {
                //质保金走完结项目
                zhibao = zhibao.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountWarranty())));
                zhibaoDatas.add(new KeyAndValueVo("[项目]"+inventoryProjectInfo.getProjectName(),inventoryProjectInfo.getAmountWarranty()));
            }
//            yinfu = yinfu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalPayable())));
//            yifu = yifu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalPaid())));
        }
        //获取当前所有的项目收支信息
        List<InventoryProjectBusiness> inventoryProjectBusinesses = getInventoryProjectBusinesssByInventoryOverview(inventoryOverviewParam);
        for(InventoryProjectBusiness inventoryProjectBusiness:inventoryProjectBusinesses){
            //通过计算收支
            if(inventoryProjectBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode())) {
                //已出纳
                if(inventoryProjectBusiness.getType().equals(StockBusinessTypeEnum.IN.getCode())) {
                    // 已收总金额 时间范围内 出纳的收入总金额
                    yishou = yishou.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
                    yishouDatas.add(new KeyAndValueVo("[项目收入]"+inventoryProjectBusiness.getId() + "_" + inventoryProjectBusiness.getSubTypeName() + "_" + inventoryProjectBusiness.getProName() + "_" + inventoryProjectBusiness.getSubTypeName() + "_" + inventoryProjectBusiness.getCreateUserName() + "_" + inventoryProjectBusiness.getCashierName()
                            ,inventoryProjectBusiness.getCashierAmount()));
                }else {
                    //已付 时间范围内 出纳的支出总金额 项目收支 日常 合同和项目提成
                    yifu = yifu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
                    yifuDatas.add(new KeyAndValueVo("[项目收入]"+inventoryProjectBusiness.getId() + "_" + inventoryProjectBusiness.getSubTypeName() + "_" + inventoryProjectBusiness.getProName() + "_" + inventoryProjectBusiness.getSubTypeName() + "_" + inventoryProjectBusiness.getCreateUserName() + "_" + inventoryProjectBusiness.getCashierName()
                            ,inventoryProjectBusiness.getCashierAmount()));
                }
            }
            //不管是审核通过还是出纳状态，直接计算审核金额
            if(inventoryProjectBusiness.getType().equals(StockBusinessTypeEnum.OUT.getCode())) {
                yinfu = yinfu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
                yinfuDatas.add(new KeyAndValueVo("[项目支出]"+inventoryProjectBusiness.getId() + "_" + inventoryProjectBusiness.getSubTypeName() + "_" + inventoryProjectBusiness.getProName() + "_" + inventoryProjectBusiness.getSubTypeName() + "_" + inventoryProjectBusiness.getCreateUserName() + "_" + inventoryProjectBusiness.getCashierName()
                        ,inventoryProjectBusiness.getCashierAmount()));
            }
        }
        //获取当前所有的日常支出信息
        List<InventoryDailyBusiness> inventoryDailyBusinesses = getInventoryDailyBusinessesByInventoryOverview(inventoryOverviewParam);
        for(InventoryDailyBusiness inventoryDailyBusiness:inventoryDailyBusinesses){
            //减去日常的支出
//            yue = yue.subtract(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
            if(inventoryDailyBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())) {
                yifu = yifu.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
                yifuDatas.add(new KeyAndValueVo("[日常支出]"+inventoryDailyBusiness.getId() + "_" + inventoryDailyBusiness.getSubTypeName() + "_" + inventoryDailyBusiness.getCreateUserName() + "_" + inventoryDailyBusiness.getCashierName()
                        ,inventoryDailyBusiness.getCashierAmount()));
            }
            yinfu = yinfu.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getAmountMoney())));
            yinfuDatas.add(new KeyAndValueVo("[日常支出]"+inventoryDailyBusiness.getId() + "_" + inventoryDailyBusiness.getSubTypeName() + "_" + inventoryDailyBusiness.getCreateUserName() + "_" + inventoryDailyBusiness.getCashierName()
                    ,inventoryDailyBusiness.getAmountMoney()));
        }

        //获取当前所有的项目其他支出信息
        List<InventoryProjectOtherBusiness> inventoryProjectOtherBusinesses = getInventoryProjectOtherBusinesssByInventoryOverview(inventoryOverviewParam);
        for(InventoryProjectOtherBusiness inventoryProjectOtherBusiness:inventoryProjectOtherBusinesses){
            //减去项目的提成的支出
//            yue = yue.subtract(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
            if(inventoryProjectOtherBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode())) {
                yifu = yifu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectOtherBusiness.getCashierAmount())));
                yifuDatas.add(new KeyAndValueVo("[项目其他支出]"+inventoryProjectOtherBusiness.getId() + "_" + inventoryProjectOtherBusiness.getSubTypeName() + "_" + inventoryProjectOtherBusiness.getCreateUserName() + "_" + inventoryProjectOtherBusiness.getCashierName()
                        ,inventoryProjectOtherBusiness.getCashierAmount()));
            }
            yinfu = yinfu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectOtherBusiness.getAmountMoney())));
            yinfuDatas.add(new KeyAndValueVo("[项目其他支出]"+inventoryProjectOtherBusiness.getId() + "_" + inventoryProjectOtherBusiness.getSubTypeName() + "_" + inventoryProjectOtherBusiness.getCreateUserName() + "_" + inventoryProjectOtherBusiness.getCashierName()
                    ,inventoryProjectOtherBusiness.getAmountMoney()));
        }


        //获取当前采购合同的支出信息
        List<InventoryStockAgreement> inventoryStockAgreements = getInventoryStockAgreementByInventoryOverview(inventoryOverviewParam);
        for(InventoryStockAgreement inventoryStockAgreement:inventoryStockAgreements){
            //减去项目的提成的支出
//            yue = yue.subtract(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
            if(inventoryStockAgreement.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode())) {
                yifu = yifu.add(BigDecimal.valueOf(Double.valueOf(inventoryStockAgreement.getCashierAmount())));
                yifuDatas.add(new KeyAndValueVo("[合同支出]"+inventoryStockAgreement.getAgreementName() + "_" + inventoryStockAgreement.getAgreementTime() + "_" + inventoryStockAgreement.getCreateUserName() + "_" + inventoryStockAgreement.getCashierName()
                        ,inventoryStockAgreement.getCashierAmount()));
            }
            yinfu = yinfu.add(BigDecimal.valueOf(Double.valueOf(inventoryStockAgreement.getAgreementAmount())));
            yinfuDatas.add(new KeyAndValueVo("[合同支出]"+inventoryStockAgreement.getAgreementName() + "_" + inventoryStockAgreement.getAgreementTime() + "_" + inventoryStockAgreement.getCreateUserName() + "_" + inventoryStockAgreement.getCashierName()
                    ,inventoryStockAgreement.getAgreementAmount()));
        }


        //加上收入-支出
        yue = yue.add(yishou)
                .subtract(yifu);
        //12.未付 --- 应付-已付
        weifu = yinfu.subtract(yifu);
        yingdelirun = yingshou.subtract(yinfu);
        //获取当前所有的出库信息
        keyAndValueVos.add(new KeyAndValue2Vo("总余额(已收-已付)",yue.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),Lists.newArrayList()));
        keyAndValueVos.add(new KeyAndValue2Vo("应得利润(应收-应付)",yingdelirun.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),Lists.newArrayList()));
        keyAndValueVos.add(new KeyAndValue2Vo("应收总金额",yingshou.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),yingshouDatas));
        keyAndValueVos.add(new KeyAndValue2Vo("已收总金额",yishou.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),yishouDatas));
        keyAndValueVos.add(new KeyAndValue2Vo("质保金金额",zhibao.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),zhibaoDatas));
        keyAndValueVos.add(new KeyAndValue2Vo("应付总金额",yinfu.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),yinfuDatas));
        keyAndValueVos.add(new KeyAndValue2Vo("已付总金额",yifu.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),yifuDatas));
        keyAndValueVos.add(new KeyAndValue2Vo("未支付金额",weifu.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),Lists.newArrayList()));
        return keyAndValueVos;
    }

    private List<InventoryStockAgreement> getInventoryStockAgreementByInventoryOverview(InventoryOverviewParam inventoryOverviewParam) {
        LambdaQueryWrapper<InventoryStockAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(req->req.eq(InventoryStockAgreement::getStatus, StatusTypeEnum.CHECK_SUCCESS.getCode().toString())
                        .or().eq(InventoryStockAgreement::getStatus, StatusTypeEnum.CASHI_SUCCESS.getCode().toString()))
                .and(req -> req.between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryStockAgreement::getApproverTime
                        , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime())
                        .or().between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryStockAgreement::getCashierTime
                        , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime()));
        List<InventoryStockAgreement> inventoryDailyBusinesses = inventoryStockAgreementMapper.selectList(wrapper);
        return inventoryDailyBusinesses;
    }

    private List<InventoryProjectOtherBusiness> getInventoryProjectOtherBusinesssByInventoryOverview(InventoryOverviewParam inventoryOverviewParam) {
        LambdaQueryWrapper<InventoryProjectOtherBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(req->req.eq(InventoryProjectOtherBusiness::getStatus, StatusTypeEnum.CHECK_SUCCESS.getCode().toString())
                        .or().eq(InventoryProjectOtherBusiness::getStatus, StatusTypeEnum.CASHI_SUCCESS.getCode().toString()))
                .and(req -> req.between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectOtherBusiness::getCreateTime
                                , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime())
                        .or().between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectOtherBusiness::getCashierTime
                                , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime()));
        List<InventoryProjectOtherBusiness> inventoryProjectOtherBusinesses = inventoryProjectOtherBusinessMapper.selectList(wrapper);
        return inventoryProjectOtherBusinesses;
    }

    private List<InventoryProjectBusiness> getInventoryProjectBusinesssByInventoryOverview(InventoryOverviewParam inventoryOverviewParam) {
        LambdaQueryWrapper<InventoryProjectBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(req->req.eq(InventoryProjectBusiness::getStatus, StatusTypeEnum.CHECK_SUCCESS.getCode().toString())
                        .or().eq(InventoryProjectBusiness::getStatus, StatusTypeEnum.CASHI_SUCCESS.getCode().toString()))
                .and(req -> req.between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectBusiness::getApproverTime
                                , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime())
                        .or().between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectBusiness::getCashierTime
                                , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime()));
        List<InventoryProjectBusiness> inventoryDailyBusinesses = inventoryProjectBusinessMapper.selectList(wrapper);
        return inventoryDailyBusinesses;
    }

    /**
     * 获取日常的支出列表
     * @param inventoryOverviewParam
     * @return
     */
    private List<InventoryDailyBusiness> getInventoryDailyBusinessesByInventoryOverview(InventoryOverviewParam inventoryOverviewParam) {
        LambdaQueryWrapper<InventoryDailyBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(req->req.eq(InventoryDailyBusiness::getStatus, StatusTypeEnum.CHECK_SUCCESS.getCode().toString())
                        .or().eq(InventoryDailyBusiness::getStatus, StatusTypeEnum.CASHI_SUCCESS.getCode().toString()))
                .and(req -> req.between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryDailyBusiness::getApproverTime
                                , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime())
                        .or().between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryDailyBusiness::getCashierTime
                                , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime()));
        List<InventoryDailyBusiness> inventoryDailyBusinesses = inventoryDailyBusinessMapper.selectList(wrapper);
        return inventoryDailyBusinesses;
    }

    /**
     * 根据日期获取相应的项目信息
     * @param inventoryOverviewParam
     * @return
     */
    private List<InventoryProjectInfo> getInventoryProjectInfosByInventoryOverview(InventoryOverviewParam inventoryOverviewParam,Boolean isFinished,Boolean isTimer) {
        //获取当前所有的项目信息
        LambdaQueryWrapper<InventoryProjectInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(isFinished,InventoryProjectInfo::getStep, StepTypeEnum.FINISHED.getCode().toString())
                .between(isTimer&&Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectInfo::getCreateTime
                        , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime());
        List<InventoryProjectInfo> inventoryProjectInfos = inventoryProjectInfoMapper.selectList(wrapper);
        return inventoryProjectInfos;
    }

    /**
     * 根据日期获取相应的项目信息
     * @param inventoryOverviewParam
     * @return
     */
    private List<InventoryProjectBusiness> getInventoryProjectBusinessByInventoryOverview(InventoryOverviewParam inventoryOverviewParam,Boolean isFinished,Boolean isTimer) {
        //获取当前所有的项目信息
        LambdaQueryWrapper<InventoryProjectBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(isFinished,InventoryProjectBusiness::getStatus, StatusTypeEnum.CASHI_SUCCESS.getCode().toString())
                .between(isTimer&&Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectBusiness::getCashierTime
                        , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime());
        List<InventoryProjectBusiness> inventoryProjectBusinesses = inventoryProjectBusinessMapper.selectList(wrapper);
        return inventoryProjectBusinesses;
    }

    @Override
    public List<KeyAndValueVo> getDailyFinance(InventoryOverviewParam inventoryOverviewParam) {
        //判断一下当前时间是否为null，默认填充最近7天的日期
        isNullForInventoryOverviewParam(inventoryOverviewParam);
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        List<InventoryDailyBusiness> inventoryProjectInfosByInventoryOverview = getInventoryDailyBusinessesByInventoryOverview(inventoryOverviewParam);
        Map<String, List<InventoryDailyBusiness>> dailyMap = new LinkedHashMap<String, List<InventoryDailyBusiness>>();
        CommonListUtils.listGroup2Map(inventoryProjectInfosByInventoryOverview, dailyMap, InventoryDailyBusiness.class, "getSubTypeName");// 输入方法名
        //查看所有的子收支类型
        LambdaQueryWrapper<InventoryDailyType> wrapper = new LambdaQueryWrapper<>();
        List<InventoryDailyType> dataLists = inventoryDailyTypeMapper.selectList(wrapper);
        for(InventoryDailyType syDictData:dataLists) {
            if (dailyMap.containsKey(syDictData.getDailyName())) {
                BigDecimal amount = new BigDecimal(0);
                for (InventoryDailyBusiness inventoryDailyBusiness : dailyMap.get(syDictData.getDailyName())) {
                    if(inventoryDailyBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())) {
                        amount = amount.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
                    }
                }
                keyAndValueVos.add(new KeyAndValueVo(syDictData.getDailyName(), amount.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            } else {
                keyAndValueVos.add(new KeyAndValueVo(syDictData.getDailyName(), "0"));
            }
        }
        return keyAndValueVos;
    }

    @Override
    public List<KeyAndValue2Vo> getManageFinance(InventoryOverviewParam inventoryOverviewParam) {
        //判断一下当前时间是否为null，默认填充最近7天的日期
        isNullForInventoryOverviewParam(inventoryOverviewParam);
        List<KeyAndValue2Vo> keyAndValueVos = Lists.newArrayList();
        BigDecimal xiangmushouru = new BigDecimal(0);
        BigDecimal xiangmuzhichu = new BigDecimal(0);
        BigDecimal richangzhichu = new BigDecimal(0);
        BigDecimal hetongcaigou = new BigDecimal(0);
        BigDecimal hetongchuku = new BigDecimal(0);
        BigDecimal xiangmuqitazhichu = new BigDecimal(0);
        List<KeyAndValueVo> xshouru = Lists.newArrayList();
        List<KeyAndValueVo> xzhichu = Lists.newArrayList();
        List<KeyAndValueVo> rzhichu = Lists.newArrayList();
        List<KeyAndValueVo> htcaigou = Lists.newArrayList();
        List<KeyAndValueVo> htchuku = Lists.newArrayList();
        List<KeyAndValueVo> xmqtzhichu = Lists.newArrayList();
        Map<String,BigDecimal> xiangmuInMap = Maps.newConcurrentMap();
        Map<String,BigDecimal> xiangmuOutMap = Maps.newConcurrentMap();
        List<InventoryProjectBusiness> inventoryProjectBusinesses = getInventoryProjectBusinessByInventoryOverview(inventoryOverviewParam,true,true);
        for(InventoryProjectBusiness business:inventoryProjectBusinesses){
            if(business.getType().equals(StockBusinessTypeEnum.IN.getCode())) {
                xiangmushouru = xiangmushouru.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                BigDecimal old = xiangmuInMap.getOrDefault(business.getSubTypeName(),new BigDecimal("0"));
                old = old.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                xiangmuInMap.put(business.getSubTypeName(),old);
            }else{
                xiangmuzhichu = xiangmuzhichu.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                BigDecimal old = xiangmuOutMap.getOrDefault(business.getSubTypeName(),new BigDecimal("0"));
                old = old.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                xiangmuOutMap.put(business.getSubTypeName(),old);
            }
        }
        for(Map.Entry<String,BigDecimal> entry:xiangmuInMap.entrySet()){
            xshouru.add(new KeyAndValueVo(entry.getKey(),entry.getValue().divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }
        for(Map.Entry<String,BigDecimal> entry:xiangmuOutMap.entrySet()){
            xzhichu.add(new KeyAndValueVo(entry.getKey(),entry.getValue().divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }
        
        //日常支出
        Map<String,BigDecimal> dailyMap = Maps.newConcurrentMap();
        List<InventoryDailyBusiness> inventoryDailyBusinesses = getInventoryDailyBusinessesByInventoryOverview(inventoryOverviewParam);
        for(InventoryDailyBusiness business:inventoryDailyBusinesses){
            if(business.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())) {
                richangzhichu = richangzhichu.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                BigDecimal old = dailyMap.getOrDefault(business.getSubTypeName(), new BigDecimal("0"));
                old = old.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                dailyMap.put(business.getSubTypeName(), old);
            }
        }
        for(Map.Entry<String,BigDecimal> entry:dailyMap.entrySet()){
            rzhichu.add(new KeyAndValueVo(entry.getKey(),entry.getValue().divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }

        //合同收支
        List<InventoryStockAgreement> inventoryStockAgreements = getInventoryStockAgreementByInventoryOverview(inventoryOverviewParam);
        for(InventoryStockAgreement stockAgreement:inventoryStockAgreements){
            if(stockAgreement.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())) {
                if (stockAgreement.getType().equals(StockBusinessTypeEnum.IN.getCode())) {
                    //采购 == 支出
                    hetongcaigou = hetongcaigou.add(BigDecimal.valueOf(Double.valueOf(stockAgreement.getCashierAmount())));
                    htcaigou.add(new KeyAndValueVo(stockAgreement.getAgreementName(),stockAgreement.getCashierAmount()));
                } else {
                    //出库
                    hetongchuku = hetongchuku.add(BigDecimal.valueOf(Double.valueOf(stockAgreement.getCashierAmount())));
                    htchuku.add(new KeyAndValueVo(stockAgreement.getAgreementName(),stockAgreement.getCashierAmount()));
                }
            }
        }
        //项目其他支出
        Map<String,BigDecimal> proOtherMap = Maps.newConcurrentMap();
        List<InventoryProjectOtherBusiness> inventoryProjectOtherBusinesses = getInventoryProjectOtherBusinessByInventoryOverview(inventoryOverviewParam);
        for(InventoryProjectOtherBusiness business:inventoryProjectOtherBusinesses){
            if(business.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())) {
                xiangmuqitazhichu = xiangmuqitazhichu.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                BigDecimal old = proOtherMap.getOrDefault(business.getSubTypeName(), new BigDecimal("0"));
                old = old.add(BigDecimal.valueOf(Double.valueOf(business.getCashierAmount())));
                proOtherMap.put(business.getSubTypeName(), old);
            }
        }
        for(Map.Entry<String,BigDecimal> entry:proOtherMap.entrySet()){
            xmqtzhichu.add(new KeyAndValueVo(entry.getKey(),entry.getValue().divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }

        keyAndValueVos.add(new KeyAndValue2Vo("项目总收入",xiangmushouru.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),xshouru));
        keyAndValueVos.add(new KeyAndValue2Vo("项目总支出",xiangmuzhichu.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),xzhichu));
        keyAndValueVos.add(new KeyAndValue2Vo("项目其他支出",xiangmuqitazhichu.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),xmqtzhichu));
        keyAndValueVos.add(new KeyAndValue2Vo("日常总支出",richangzhichu.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),rzhichu));
        keyAndValueVos.add(new KeyAndValue2Vo("采购合同",hetongcaigou.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),htcaigou));
        keyAndValueVos.add(new KeyAndValue2Vo("出库合同",hetongchuku.divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),htchuku));
        return keyAndValueVos;
    }

    private List<InventoryProjectOtherBusiness> getInventoryProjectOtherBusinessByInventoryOverview(InventoryOverviewParam inventoryOverviewParam) {
        //获取当前所有的项目信息
        LambdaQueryWrapper<InventoryProjectOtherBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryProjectOtherBusiness::getStatus, StatusTypeEnum.CASHI_SUCCESS.getCode().toString())
                .between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectOtherBusiness::getCashierTime
                        , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime());
        List<InventoryProjectOtherBusiness> inventoryProjectOtherBusinesses = inventoryProjectOtherBusinessMapper.selectList(wrapper);
        return inventoryProjectOtherBusinesses;
    }

    @Override
    public List<KeyAndValueVo> getProjectStatusFinance(InventoryOverviewParam inventoryOverviewParam) {
        //判断一下当前时间是否为null，默认填充最近7天的日期
        isNullForInventoryOverviewParam(inventoryOverviewParam);
        List<InventoryProjectInfo> inventoryProjectInfosByInventoryOverview = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,false,true);
        Map<String, List<InventoryProjectInfo>> projectMap = new LinkedHashMap<String, List<InventoryProjectInfo>>();
        CommonListUtils.listGroup2Map(inventoryProjectInfosByInventoryOverview, projectMap, InventoryProjectInfo.class, "getStep");// 输入方法名
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        keyAndValueVos.add(new KeyAndValueVo(StepTypeEnum.NEW.getDesc(),String.valueOf(projectMap.getOrDefault(StepTypeEnum.NEW.getCode().toString(),Lists.newArrayList()).size())));
        keyAndValueVos.add(new KeyAndValueVo(StepTypeEnum.PRE_SALES.getDesc(),String.valueOf(projectMap.getOrDefault(StepTypeEnum.PRE_SALES.getCode().toString(),Lists.newArrayList()).size())));
        keyAndValueVos.add(new KeyAndValueVo(StepTypeEnum.RUNNING.getDesc(),String.valueOf(projectMap.getOrDefault(StepTypeEnum.RUNNING.getCode().toString(),Lists.newArrayList()).size())));
        keyAndValueVos.add(new KeyAndValueVo(StepTypeEnum.FINISHED.getDesc(),String.valueOf(projectMap.getOrDefault(StepTypeEnum.FINISHED.getCode().toString(),Lists.newArrayList()).size())));
        return keyAndValueVos;
    }

    @Override
    public EChartVo getExpensesAndEeceiptsFinance(InventoryOverviewParam inventoryOverviewParam) {
        LoginSysUserRedisVo loginSysUserRedisVo = loginRedisService.getLoginSysUserRedisVo(JwtUtil.getUsername(JwtTokenUtil.getToken()));
        Long departmentId = loginSysUserRedisVo.getDepartmentId();
        //判断一下当前时间是否为null，默认填充最近7天的日期
        isNullForInventoryOverviewParam(inventoryOverviewParam);
        EChartVo eChartVo = new EChartVo();
        //获取时间范围内的公司的出纳的项目收支数据和日常支出数据
        List<CompanyCashierVo> companyCashierByDates = inventoryProjectInfoMapper.getCompanyCashierByDates(inventoryOverviewParam,departmentId);
        List<String> xList = Lists.newArrayList();
        EChartSeriesVo shouru = new EChartSeriesVo();
        EChartSeriesVo zhichu = new EChartSeriesVo();
        List<String> shouruLists = Lists.newArrayList();
        List<String> zhichuLists = Lists.newArrayList();
        for(CompanyCashierVo companyCashierVo:companyCashierByDates){
            xList.add(companyCashierVo.getRiqi());
            shouruLists.add(companyCashierVo.getShouru());
            zhichuLists.add(companyCashierVo.getZhichu());
        }
        shouru.setData(shouruLists);
        shouru.setName("收入");
        zhichu.setData(zhichuLists);
        zhichu.setName("支出");
        EChartXAxisVo eChartXAxisVo = new EChartXAxisVo();
        eChartXAxisVo.setName("日期");
        eChartXAxisVo.setData(xList);
        eChartVo.setXAxis(eChartXAxisVo);
        List<EChartSeriesVo> ydata = Lists.newArrayList();
        ydata.add(shouru);
        ydata.add(zhichu);
        eChartVo.setSeries(ydata);
        return eChartVo;
    }

    @Override
    public List<KeyAndValueVo> getProfitFinance(InventoryOverviewParam inventoryOverviewParam) {
        //判断一下当前时间是否为null，默认填充最近7天的日期
        isNullForInventoryOverviewParam(inventoryOverviewParam);
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        List<InventoryProjectInfo> inventoryProjectInfos = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,true,false);
        for(InventoryProjectInfo inventoryProjectInfo:inventoryProjectInfos){
            keyAndValueVos.add(new KeyAndValueVo(inventoryProjectInfo.getProjectName(),
                    BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountProfitNet())).divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }
        return keyAndValueVos;
    }

    @Override
    public List<CollectionStatisticsVo> getReceivablesFinance(InventoryOverviewParam inventoryOverviewParam) {
        //判断一下当前时间是否为null，默认填充最近7天的日期
        isNullForInventoryOverviewParam(inventoryOverviewParam);
        List<CollectionStatisticsVo> collectionStatisticsVos = Lists.newArrayList();
        List<InventoryProjectInfo> inventoryProjectInfos = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,false,false);
        for(InventoryProjectInfo inventoryProjectInfo:inventoryProjectInfos){
            collectionStatisticsVos.add(new CollectionStatisticsVo(inventoryProjectInfo.getProjectName()
                    ,BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalReceivables())).divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()
                    ,BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalReceived())).divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()
                    ,BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalUnreceived())).divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }
        return collectionStatisticsVos;
    }

    /**
     * 判断时间是否为null 否则默认查询最近7天的数据
     * @param inventoryOverviewParam
     */
    private void isNullForInventoryOverviewParam(InventoryOverviewParam inventoryOverviewParam) {
        if(null == inventoryOverviewParam.getStarTime() || null == inventoryOverviewParam.getEndTime()){
            inventoryOverviewParam.setEndTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            inventoryOverviewParam.setStarTime(LocalDateTime.now().plusMonths(-6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }else{
            //获取当前月的天数
            inventoryOverviewParam.setEndTime(LocalDateTime.parse(inventoryOverviewParam.getEndTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
            inventoryOverviewParam.setStarTime(LocalDateTime.parse(inventoryOverviewParam.getStarTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-01 00:00:00")));
        }
    }

}
