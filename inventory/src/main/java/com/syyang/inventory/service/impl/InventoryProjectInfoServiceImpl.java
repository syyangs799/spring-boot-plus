package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.mapper.InventoryProjectInfoMapper;
import com.syyang.inventory.service.InventoryProjectInfoService;
import com.syyang.inventory.param.InventoryProjectInfoPageParam;
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
 * 项目信息表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryProjectInfoServiceImpl extends BaseServiceImpl<InventoryProjectInfoMapper, InventoryProjectInfo> implements InventoryProjectInfoService {

    @Autowired
    private InventoryProjectInfoMapper inventoryProjectInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception {
        return super.save(inventoryProjectInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception {
        return super.updateById(inventoryProjectInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProjectInfo(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProjectInfo> getInventoryProjectInfoPageList(InventoryProjectInfoPageParam inventoryProjectInfoPageParam) throws Exception {
        Page<InventoryProjectInfo> page = new PageInfo<>(inventoryProjectInfoPageParam, OrderItem.desc(getLambdaColumn(InventoryProjectInfo::getCreateTime)));
        LambdaQueryWrapper<InventoryProjectInfo> wrapper = new LambdaQueryWrapper<>();
        IPage<InventoryProjectInfo> iPage = inventoryProjectInfoMapper.selectPage(page, wrapper);
        return new Paging<InventoryProjectInfo>(iPage);
    }

}
