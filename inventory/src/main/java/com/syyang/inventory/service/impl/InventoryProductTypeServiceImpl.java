package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryProductType;
import com.syyang.inventory.mapper.InventoryProductTypeMapper;
import com.syyang.inventory.service.InventoryProductTypeService;
import com.syyang.inventory.param.InventoryProductTypePageParam;
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
 * 产品类型码表 服务实现类
 *
 * @author syyang
 * @since 2023-04-01
 */
@Slf4j
@Service
public class InventoryProductTypeServiceImpl extends BaseServiceImpl<InventoryProductTypeMapper, InventoryProductType> implements InventoryProductTypeService {

    @Autowired
    private InventoryProductTypeMapper inventoryProductTypeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProductType(InventoryProductType inventoryProductType) throws Exception {
        return super.save(inventoryProductType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProductType(InventoryProductType inventoryProductType) throws Exception {
        return super.updateById(inventoryProductType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProductType(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProductType> getInventoryProductTypePageList(InventoryProductTypePageParam inventoryProductTypePageParam) throws Exception {
        Page<InventoryProductType> page = new PageInfo<>(inventoryProductTypePageParam, OrderItem.desc(getLambdaColumn(InventoryProductType::getCreateTime)));
        LambdaQueryWrapper<InventoryProductType> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryProductType> iPage = inventoryProductTypeMapper.selectPage(page, wrapper);
        iPage.setTotal(inventoryProductTypeMapper.selectCount(wrapper));
        return new Paging<InventoryProductType>(iPage);
    }
    @Override
    public List<InventoryProductType> getInventoryProductTypeList(InventoryProductTypePageParam inventoryProductTypePageParam) throws Exception {
        LambdaQueryWrapper<InventoryProductType> wrapper = new LambdaQueryWrapper<>();
        List<InventoryProductType> dataLists = inventoryProductTypeMapper.selectList(wrapper);
        return dataLists;
    }



}
