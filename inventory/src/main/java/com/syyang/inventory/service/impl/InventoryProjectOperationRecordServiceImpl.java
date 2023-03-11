package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.mapper.InventoryProjectOperationRecordMapper;
import com.syyang.inventory.service.InventoryProjectOperationRecordService;
import com.syyang.inventory.param.InventoryProjectOperationRecordPageParam;
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
 * 项目修改记录表 服务实现类
 *
 * @author syyang
 * @since 2023-03-11
 */
@Slf4j
@Service
public class InventoryProjectOperationRecordServiceImpl extends BaseServiceImpl<InventoryProjectOperationRecordMapper, InventoryProjectOperationRecord> implements InventoryProjectOperationRecordService {

    @Autowired
    private InventoryProjectOperationRecordMapper inventoryProjectOperationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectOperationRecord(InventoryProjectOperationRecord inventoryProjectOperationRecord) throws Exception {
        return super.save(inventoryProjectOperationRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectOperationRecord(InventoryProjectOperationRecord inventoryProjectOperationRecord) throws Exception {
        return super.updateById(inventoryProjectOperationRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProjectOperationRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryProjectOperationRecord> getInventoryProjectOperationRecordPageList(InventoryProjectOperationRecordPageParam inventoryProjectOperationRecordPageParam) throws Exception {
        Page<InventoryProjectOperationRecord> page = new PageInfo<>(inventoryProjectOperationRecordPageParam, OrderItem.desc(getLambdaColumn(InventoryProjectOperationRecord::getUpdateTime)));
        LambdaQueryWrapper<InventoryProjectOperationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null!= inventoryProjectOperationRecordPageParam.getProjectId(),InventoryProjectOperationRecord::getProjectId,inventoryProjectOperationRecordPageParam.getProjectId());
        IPage<InventoryProjectOperationRecord> iPage = inventoryProjectOperationRecordMapper.selectPage(page, wrapper);
        return new Paging<InventoryProjectOperationRecord>(iPage);
    }
    @Override
    public List<InventoryProjectOperationRecord> getInventoryProjectOperationRecordList(InventoryProjectOperationRecordPageParam inventoryProjectOperationRecordPageParam) throws Exception {
        LambdaQueryWrapper<InventoryProjectOperationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null!= inventoryProjectOperationRecordPageParam.getProjectId(),InventoryProjectOperationRecord::getProjectId,inventoryProjectOperationRecordPageParam.getProjectId());
        List<InventoryProjectOperationRecord> dataLists = inventoryProjectOperationRecordMapper.selectList(wrapper);
        return dataLists;
    }



}
