package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.mapper.InventoryProductInfoMapper;
import com.syyang.inventory.service.InventoryProductInfoService;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
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
 * 产品信息表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryProductInfoServiceImpl extends BaseServiceImpl<InventoryProductInfoMapper, InventoryProductInfo> implements InventoryProductInfoService {

    @Autowired
    private InventoryProductInfoMapper inventoryProductInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProductInfo(InventoryProductInfo inventoryProductInfo) throws Exception {
        return super.save(inventoryProductInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProductInfo(InventoryProductInfo inventoryProductInfo) throws Exception {
        return super.updateById(inventoryProductInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProductInfo(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProductInfo> getInventoryProductInfoPageList(InventoryProductInfoPageParam inventoryProductInfoPageParam) throws Exception {
        Page<InventoryProductInfo> page = new PageInfo<>(inventoryProductInfoPageParam, OrderItem.desc(getLambdaColumn(InventoryProductInfo::getCreateTime)));
        LambdaQueryWrapper<InventoryProductInfo> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryProductInfo> iPage = inventoryProductInfoMapper.selectPage(page, wrapper);
        return new Paging<InventoryProductInfo>(iPage);
    }

}
