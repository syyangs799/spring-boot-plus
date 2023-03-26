package com.syyang.inventory.service.impl;

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
import com.syyang.inventory.mapper.InventoryDailyBusinessMapper;
import com.syyang.inventory.mapper.InventoryProductInfoMapper;
import com.syyang.inventory.mapper.InventoryProjectInfoMapper;
import com.syyang.inventory.mapper.SyDictDataMapper;
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
    private SyDictDataMapper syDictDataMapper;
    @Autowired
    private InventoryDailyBusinessMapper inventoryDailyBusinessMapper;
    @Autowired
    private InventoryProjectInfoMapper inventoryProjectInfoMapper;
    @Autowired
    private LoginRedisService loginRedisService;

    @Override
    public List<KeyAndValueVo> getProjectFinance(InventoryOverviewParam inventoryOverviewParam) {
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        List<InventoryProjectInfo> inventoryProjectInfos = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,true);
        BigDecimal yue = new BigDecimal(0);
        BigDecimal yingshou = new BigDecimal(0);
        BigDecimal yishou = new BigDecimal(0);
        BigDecimal zhibao = new BigDecimal(0);
        BigDecimal yinfu = new BigDecimal(0);
        BigDecimal yifu = new BigDecimal(0);
        BigDecimal weifu = new BigDecimal(0);
        for(InventoryProjectInfo inventoryProjectInfo:inventoryProjectInfos){
            yingshou = yingshou.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalReceivables())));
            yishou = yishou.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalReceived())));
            zhibao = zhibao.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getAmountWarranty())));
            yinfu = yinfu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalPayable())));
            yifu = yifu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalPaid())));
            weifu = weifu.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectInfo.getTotalUnpaid())));
            //加上收入-支出
            yue = yue.add(yishou)
                    .subtract(yifu);
        }
        //获取当前所有的项目收支信息
        //获取当前所有的日常支出信息
        List<InventoryDailyBusiness> inventoryDailyBusinesses = getInventoryDailyBusinessesByInventoryOverview(inventoryOverviewParam);
        for(InventoryDailyBusiness inventoryDailyBusiness:inventoryDailyBusinesses){
            //减去日常的支出
            yue = yue.subtract(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
        }
        //获取当前所有的出库信息
        keyAndValueVos.add(new KeyAndValueVo("总余额",yue.toString()));
        keyAndValueVos.add(new KeyAndValueVo("应收总金额",yingshou.toString()));
        keyAndValueVos.add(new KeyAndValueVo("已收总金额",yishou.toString()));
        keyAndValueVos.add(new KeyAndValueVo("质保金金额",zhibao.toString()));
        keyAndValueVos.add(new KeyAndValueVo("应付总金额",yinfu.toString()));
        keyAndValueVos.add(new KeyAndValueVo("已付总金额",yifu.toString()));
        keyAndValueVos.add(new KeyAndValueVo("未支付金额",weifu.toString()));
        return keyAndValueVos;
    }

    /**
     * 获取日常的支出列表
     * @param inventoryOverviewParam
     * @return
     */
    private List<InventoryDailyBusiness> getInventoryDailyBusinessesByInventoryOverview(InventoryOverviewParam inventoryOverviewParam) {
        LambdaQueryWrapper<InventoryDailyBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryDailyBusiness::getStatus, StatusTypeEnum.CASHI_SUCCESS.getCode().toString())
                .between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryDailyBusiness::getCashierTime
                        , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime());
        List<InventoryDailyBusiness> inventoryDailyBusinesses = inventoryDailyBusinessMapper.selectList(wrapper);
        return inventoryDailyBusinesses;
    }

    /**
     * 根据日期获取相应的项目信息
     * @param inventoryOverviewParam
     * @return
     */
    private List<InventoryProjectInfo> getInventoryProjectInfosByInventoryOverview(InventoryOverviewParam inventoryOverviewParam,Boolean isFinished) {
        //获取当前所有的项目信息
        LambdaQueryWrapper<InventoryProjectInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(isFinished,InventoryProjectInfo::getStep, StepTypeEnum.FINISHED.getCode().toString())
                .between(Objects.nonNull(inventoryOverviewParam.getStarTime()),InventoryProjectInfo::getCreateTime
                        , inventoryOverviewParam.getStarTime(), inventoryOverviewParam.getEndTime());
        List<InventoryProjectInfo> inventoryProjectInfos = inventoryProjectInfoMapper.selectList(wrapper);
        return inventoryProjectInfos;
    }

    @Override
    public List<KeyAndValueVo> getDailyFinance(InventoryOverviewParam inventoryOverviewParam) {
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        List<InventoryDailyBusiness> inventoryProjectInfosByInventoryOverview = getInventoryDailyBusinessesByInventoryOverview(inventoryOverviewParam);
        Map<String, List<InventoryDailyBusiness>> dailyMap = new LinkedHashMap<String, List<InventoryDailyBusiness>>();
        CommonListUtils.listGroup2Map(inventoryProjectInfosByInventoryOverview, dailyMap, InventoryDailyBusiness.class, "getSubTypeName");// 输入方法名

        //查看所有的子收支类型
        LambdaQueryWrapper<SyDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SyDictData::getDictType,"daily_sub_type");
        List<SyDictData> dataLists = syDictDataMapper.selectList(wrapper);
        for(SyDictData syDictData:dataLists) {
            if (dailyMap.containsKey(syDictData.getLabel())) {
                BigDecimal amount = new BigDecimal(0);
                for (InventoryDailyBusiness inventoryDailyBusiness : dailyMap.get(syDictData.getLabel())) {
                    amount = amount.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
                }
                keyAndValueVos.add(new KeyAndValueVo(syDictData.getLabel(), amount.toString()));
            } else {
                keyAndValueVos.add(new KeyAndValueVo(syDictData.getLabel(), "0"));
            }
        }
        return keyAndValueVos;
    }

    @Override
    public List<KeyAndValueVo> getManageFinance(InventoryOverviewParam inventoryOverviewParam) {
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        keyAndValueVos.add(new KeyAndValueVo("收入总额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("项目含税开支总额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("项目不含税开支总额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("办公费用开支总额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("财务费开支总额","5550000"));
        return keyAndValueVos;
    }

    @Override
    public List<KeyAndValueVo> getProjectStatusFinance(InventoryOverviewParam inventoryOverviewParam) {
        List<InventoryProjectInfo> inventoryProjectInfosByInventoryOverview = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,false);
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
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        List<InventoryProjectInfo> inventoryProjectInfos = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,true);
        for(InventoryProjectInfo inventoryProjectInfo:inventoryProjectInfos){
            keyAndValueVos.add(new KeyAndValueVo(inventoryProjectInfo.getProjectName(),inventoryProjectInfo.getAmountProfitNet()));
        }
        return keyAndValueVos;
    }

    @Override
    public List<CollectionStatisticsVo> getReceivablesFinance(InventoryOverviewParam inventoryOverviewParam) {
        List<CollectionStatisticsVo> collectionStatisticsVos = Lists.newArrayList();
        List<InventoryProjectInfo> inventoryProjectInfos = getInventoryProjectInfosByInventoryOverview(inventoryOverviewParam,true);
        for(InventoryProjectInfo inventoryProjectInfo:inventoryProjectInfos){
            collectionStatisticsVos.add(new CollectionStatisticsVo(inventoryProjectInfo.getProjectName()
                    ,inventoryProjectInfo.getTotalReceivables()
                    ,inventoryProjectInfo.getTotalReceived()
                    ,inventoryProjectInfo.getTotalUnreceived()));
        }
        return collectionStatisticsVos;
    }

    /**
     * 判断时间是否为null 否则默认查询最近7天的数据
     * @param inventoryOverviewParam
     */
    private void isNullForInventoryOverviewParam(InventoryOverviewParam inventoryOverviewParam) {
        if(null == inventoryOverviewParam.getStarTime() || null == inventoryOverviewParam.getEndTime()){
            inventoryOverviewParam.setEndTime(LocalDateTime.now());
            inventoryOverviewParam.setStarTime(LocalDateTime.now().plusDays(-7));
        }
    }

    /**
     * 获取x轴
     * @param inventoryOverviewParam
     * @return
     */
    private List<String> getXDateList(InventoryOverviewParam inventoryOverviewParam) {
        return Lists.newArrayList();
    }
}
