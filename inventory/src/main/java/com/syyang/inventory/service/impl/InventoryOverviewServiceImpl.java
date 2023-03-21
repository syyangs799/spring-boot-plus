package com.syyang.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.mapper.InventoryProductInfoMapper;
import com.syyang.inventory.param.InventoryOverviewParam;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
import com.syyang.inventory.service.InventoryOverviewService;
import com.syyang.inventory.service.InventoryProductInfoService;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Override
    public List<KeyAndValueVo> getProjectFinance(InventoryOverviewParam inventoryOverviewParam) {
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        keyAndValueVos.add(new KeyAndValueVo("总余额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("应收总金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("已收总金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("未收总金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("质保金金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("应付总金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("未支付金额","5550000"));
        return keyAndValueVos;
    }

    @Override
    public List<KeyAndValueVo> getDailyFinance(InventoryOverviewParam inventoryOverviewParam) {
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        keyAndValueVos.add(new KeyAndValueVo("开支总金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("办公室总金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("人员工资总金额","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("车辆费用","5550000"));
        keyAndValueVos.add(new KeyAndValueVo("财务费","5550000"));
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
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        keyAndValueVos.add(new KeyAndValueVo("售前","10"));
        keyAndValueVos.add(new KeyAndValueVo("实施","222"));
        keyAndValueVos.add(new KeyAndValueVo("售后","333"));
        keyAndValueVos.add(new KeyAndValueVo("完成","33"));
        return keyAndValueVos;
    }

    @Override
    public Map<String, List<KeyAndValueVo>> getExpensesAndEeceiptsFinance(InventoryOverviewParam inventoryOverviewParam) {
        Map<String, List<KeyAndValueVo>> map = Maps.newHashMap();
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        keyAndValueVos.add(new KeyAndValueVo("日常","10"));
        keyAndValueVos.add(new KeyAndValueVo("项目A","222"));
        keyAndValueVos.add(new KeyAndValueVo("项目B","333"));
        keyAndValueVos.add(new KeyAndValueVo("项目C","33"));
        map.put("收入",keyAndValueVos);

        List<KeyAndValueVo> keyAndValueVos2 = Lists.newArrayList();
        keyAndValueVos2.add(new KeyAndValueVo("日常","10"));
        keyAndValueVos2.add(new KeyAndValueVo("项目C","222"));
        keyAndValueVos2.add(new KeyAndValueVo("项目B","333"));
        keyAndValueVos2.add(new KeyAndValueVo("项目A","33"));
        map.put("支出",keyAndValueVos2);
        return map;
    }
}
