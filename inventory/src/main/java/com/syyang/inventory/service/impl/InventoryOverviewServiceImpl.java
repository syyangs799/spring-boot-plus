package com.syyang.inventory.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.vo.EChartVo;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.enums.StepTypeEnum;
import com.syyang.inventory.mapper.InventoryDailyBusinessMapper;
import com.syyang.inventory.mapper.InventoryProductInfoMapper;
import com.syyang.inventory.mapper.InventoryProjectInfoMapper;
import com.syyang.inventory.param.InventoryOverviewParam;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
import com.syyang.inventory.service.InventoryOverviewService;
import com.syyang.inventory.service.InventoryProductInfoService;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.util.CommonListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private InventoryProductInfoMapper inventoryProductInfoMapper;
    @Autowired
    private InventoryDailyBusinessMapper inventoryDailyBusinessMapper;
    @Autowired
    private InventoryProjectInfoMapper inventoryProjectInfoMapper;

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
        for(Map.Entry<String, List<InventoryDailyBusiness>> daily:dailyMap.entrySet()){
            BigDecimal amount = new BigDecimal(0);
            for(InventoryDailyBusiness inventoryDailyBusiness:daily.getValue()){
                amount = amount.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
            }
            keyAndValueVos.add(new KeyAndValueVo(daily.getKey(),amount.toString()));
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
        EChartVo eChartVo = new EChartVo();
//        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
//        keyAndValueVos.add(new KeyAndValueVo("日常","10"));
//        keyAndValueVos.add(new KeyAndValueVo("项目A","222"));
//        keyAndValueVos.add(new KeyAndValueVo("项目B","333"));
//        keyAndValueVos.add(new KeyAndValueVo("项目C","33"));
//        map.put("收入",keyAndValueVos);
//
//        List<KeyAndValueVo> keyAndValueVos2 = Lists.newArrayList();
//        keyAndValueVos2.add(new KeyAndValueVo("日常","10"));
//        keyAndValueVos2.add(new KeyAndValueVo("项目C","222"));
//        keyAndValueVos2.add(new KeyAndValueVo("项目B","333"));
//        keyAndValueVos2.add(new KeyAndValueVo("项目A","33"));
//        map.put("支出",keyAndValueVos2);
        //获取时间区间的横坐标
        List<String> xList = getXDateList(inventoryOverviewParam);

        eChartVo.setXAxis(null);
        eChartVo.setSeries(null);
        return eChartVo;
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
