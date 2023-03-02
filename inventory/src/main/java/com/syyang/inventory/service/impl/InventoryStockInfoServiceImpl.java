package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryStockInfo;
import com.syyang.inventory.mapper.InventoryStockInfoMapper;
import com.syyang.inventory.service.InventoryStockInfoService;
import com.syyang.inventory.param.InventoryStockInfoPageParam;
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
 * 库存信息表 服务实现类
 *
 * @author syyang
 * @since 2023-03-01
 */
@Slf4j
@Service
public class InventoryStockInfoServiceImpl extends BaseServiceImpl<InventoryStockInfoMapper, InventoryStockInfo> implements InventoryStockInfoService {

    @Autowired
    private InventoryStockInfoMapper inventoryStockInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryStockInfo(InventoryStockInfo inventoryStockInfo) throws Exception {
        return super.save(inventoryStockInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryStockInfo(InventoryStockInfo inventoryStockInfo) throws Exception {
        return super.updateById(inventoryStockInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryStockInfo(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryStockInfo> getInventoryStockInfoPageList(InventoryStockInfoPageParam inventoryStockInfoPageParam) throws Exception {
        Page<InventoryStockInfo> page = new PageInfo<>(inventoryStockInfoPageParam, OrderItem.desc(getLambdaColumn(InventoryStockInfo::getCreateTime)));
        LambdaQueryWrapper<InventoryStockInfo> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryStockInfo> iPage = inventoryStockInfoMapper.selectPage(page, wrapper);
        return new Paging<InventoryStockInfo>(iPage);
    }

}
