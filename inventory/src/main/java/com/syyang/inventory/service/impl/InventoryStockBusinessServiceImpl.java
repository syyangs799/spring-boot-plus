package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryStockBusiness;
import com.syyang.inventory.mapper.InventoryStockBusinessMapper;
import com.syyang.inventory.service.InventoryStockBusinessService;
import com.syyang.inventory.param.InventoryStockBusinessPageParam;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryStockBusiness(InventoryStockBusiness inventoryStockBusiness) throws Exception {
        return super.save(inventoryStockBusiness);
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
        IPage<InventoryStockBusiness> iPage = inventoryStockBusinessMapper.selectPage(page, wrapper);
        return new Paging<InventoryStockBusiness>(iPage);
    }

}
