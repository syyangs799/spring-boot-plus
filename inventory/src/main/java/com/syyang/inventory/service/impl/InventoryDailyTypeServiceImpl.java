package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryDailyType;
import com.syyang.inventory.mapper.InventoryDailyTypeMapper;
import com.syyang.inventory.service.InventoryDailyTypeService;
import com.syyang.inventory.param.InventoryDailyTypePageParam;
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

import java.util.List;
/**
 * 日常支出类型码表 服务实现类
 *
 * @author syyang
 * @since 2023-04-01
 */
@Slf4j
@Service
public class InventoryDailyTypeServiceImpl extends BaseServiceImpl<InventoryDailyTypeMapper, InventoryDailyType> implements InventoryDailyTypeService {

    @Autowired
    private InventoryDailyTypeMapper inventoryDailyTypeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryDailyType(InventoryDailyType inventoryDailyType) throws Exception {
        return super.save(inventoryDailyType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryDailyType(InventoryDailyType inventoryDailyType) throws Exception {
        return super.updateById(inventoryDailyType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryDailyType(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryDailyType> getInventoryDailyTypePageList(InventoryDailyTypePageParam inventoryDailyTypePageParam) throws Exception {
        Page<InventoryDailyType> page = new PageInfo<>(inventoryDailyTypePageParam, OrderItem.desc(getLambdaColumn(InventoryDailyType::getCreateTime)));
        LambdaQueryWrapper<InventoryDailyType> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryDailyType> iPage = inventoryDailyTypeMapper.selectPage(page, wrapper);
        iPage.setTotal(inventoryDailyTypeMapper.selectCount(wrapper));
        return new Paging<InventoryDailyType>(iPage);
    }
    @Override
    public List<InventoryDailyType> getInventoryDailyTypeList(InventoryDailyTypePageParam inventoryDailyTypePageParam) throws Exception {
        LambdaQueryWrapper<InventoryDailyType> wrapper = new LambdaQueryWrapper<>();
        List<InventoryDailyType> dataLists = inventoryDailyTypeMapper.selectList(wrapper);
        return dataLists;
    }



}
