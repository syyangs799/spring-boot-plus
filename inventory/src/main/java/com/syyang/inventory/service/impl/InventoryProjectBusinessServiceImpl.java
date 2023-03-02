package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.mapper.InventoryProjectBusinessMapper;
import com.syyang.inventory.service.InventoryProjectBusinessService;
import com.syyang.inventory.param.InventoryProjectBusinessPageParam;
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
 * 项目收入与支出交易流水表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryProjectBusinessServiceImpl extends BaseServiceImpl<InventoryProjectBusinessMapper, InventoryProjectBusiness> implements InventoryProjectBusinessService {

    @Autowired
    private InventoryProjectBusinessMapper inventoryProjectBusinessMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        return super.save(inventoryProjectBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        return super.updateById(inventoryProjectBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProjectBusiness(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProjectBusiness> getInventoryProjectBusinessPageList(InventoryProjectBusinessPageParam inventoryProjectBusinessPageParam) throws Exception {
        Page<InventoryProjectBusiness> page = new PageInfo<>(inventoryProjectBusinessPageParam, OrderItem.desc(getLambdaColumn(InventoryProjectBusiness::getCreateTime)));
        LambdaQueryWrapper<InventoryProjectBusiness> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryProjectBusiness> iPage = inventoryProjectBusinessMapper.selectPage(page, wrapper);
        return new Paging<InventoryProjectBusiness>(iPage);
    }

}
