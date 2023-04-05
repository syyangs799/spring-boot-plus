package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.entity.InventoryProjectOtherBusiness;
import com.syyang.inventory.mapper.InventoryProjectOtherBusinessMapper;
import com.syyang.inventory.service.InventoryProjectOtherBusinessService;
import com.syyang.inventory.param.InventoryProjectOtherBusinessPageParam;
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
 * 项目其他支出信息表 服务实现类
 *
 * @author syyang
 * @since 2023-04-05
 */
@Slf4j
@Service
public class InventoryProjectOtherBusinessServiceImpl extends BaseServiceImpl<InventoryProjectOtherBusinessMapper, InventoryProjectOtherBusiness> implements InventoryProjectOtherBusinessService {

    @Autowired
    private InventoryProjectOtherBusinessMapper inventoryProjectOtherBusinessMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectOtherBusiness(InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception {
        return super.save(inventoryProjectOtherBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectOtherBusiness(InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception {
        return super.updateById(inventoryProjectOtherBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProjectOtherBusiness(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProjectOtherBusiness> getInventoryProjectOtherBusinessPageList(InventoryProjectOtherBusinessPageParam inventoryProjectOtherBusinessPageParam) throws Exception {
        Page<InventoryProjectOtherBusiness> page = new PageInfo<>(inventoryProjectOtherBusinessPageParam, OrderItem.desc(getLambdaColumn(InventoryProjectOtherBusiness::getCreateTime)));
        LambdaQueryWrapper<InventoryProjectOtherBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != inventoryProjectOtherBusinessPageParam.getProjectId(), InventoryProjectOtherBusiness::getProjectId,inventoryProjectOtherBusinessPageParam.getProjectId());
        IPage<InventoryProjectOtherBusiness> iPage = inventoryProjectOtherBusinessMapper.selectPage(page, wrapper);
        iPage.setTotal(inventoryProjectOtherBusinessMapper.selectCount(wrapper));
        return new Paging<InventoryProjectOtherBusiness>(iPage);
    }
    @Override
    public List<InventoryProjectOtherBusiness> getInventoryProjectOtherBusinessList(InventoryProjectOtherBusinessPageParam inventoryProjectOtherBusinessPageParam) throws Exception {
        LambdaQueryWrapper<InventoryProjectOtherBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != inventoryProjectOtherBusinessPageParam.getProjectId(), InventoryProjectOtherBusiness::getProjectId,inventoryProjectOtherBusinessPageParam.getProjectId());
        List<InventoryProjectOtherBusiness> dataLists = inventoryProjectOtherBusinessMapper.selectList(wrapper);
        return dataLists;
    }



}
