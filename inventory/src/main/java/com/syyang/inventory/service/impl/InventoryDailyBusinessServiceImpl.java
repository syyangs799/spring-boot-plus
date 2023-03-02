package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.mapper.InventoryDailyBusinessMapper;
import com.syyang.inventory.service.InventoryDailyBusinessService;
import com.syyang.inventory.param.InventoryDailyBusinessPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 日常收入与支出交易流水表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryDailyBusinessServiceImpl extends BaseServiceImpl<InventoryDailyBusinessMapper, InventoryDailyBusiness> implements InventoryDailyBusinessService {

    @Autowired
    private InventoryDailyBusinessMapper inventoryDailyBusinessMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryDailyBusiness(InventoryDailyBusiness inventoryDailyBusiness) throws Exception {
        return super.save(inventoryDailyBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryDailyBusiness(InventoryDailyBusiness inventoryDailyBusiness) throws Exception {
        return super.updateById(inventoryDailyBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryDailyBusiness(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryDailyBusiness> getInventoryDailyBusinessPageList(InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) throws Exception {
        Page<InventoryDailyBusiness> page = new PageInfo<>(inventoryDailyBusinessPageParam, OrderItem.desc(getLambdaColumn(InventoryDailyBusiness::getCreateTime)));
        LambdaQueryWrapper<InventoryDailyBusiness> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryDailyBusiness> iPage = inventoryDailyBusinessMapper.selectPage(page, wrapper);
        return new Paging<InventoryDailyBusiness>(iPage);
    }

}
