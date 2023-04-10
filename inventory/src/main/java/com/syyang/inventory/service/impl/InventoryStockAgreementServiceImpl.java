package com.syyang.inventory.service.impl;

import cn.hutool.core.util.StrUtil;
import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.entity.InventoryStockAgreement;
import com.syyang.inventory.mapper.InventoryStockAgreementMapper;
import com.syyang.inventory.service.InventoryStockAgreementService;
import com.syyang.inventory.param.InventoryStockAgreementPageParam;
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
 * 库存合同表 服务实现类
 *
 * @author syyang
 * @since 2023-04-05
 */
@Slf4j
@Service
public class InventoryStockAgreementServiceImpl extends BaseServiceImpl<InventoryStockAgreementMapper, InventoryStockAgreement> implements InventoryStockAgreementService {

    @Autowired
    private InventoryStockAgreementMapper inventoryStockAgreementMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryStockAgreement(InventoryStockAgreement inventoryStockAgreement) throws Exception {
        return super.save(inventoryStockAgreement);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryStockAgreement(InventoryStockAgreement inventoryStockAgreement) throws Exception {
        return super.updateById(inventoryStockAgreement);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryStockAgreement(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryStockAgreement> getInventoryStockAgreementPageList(InventoryStockAgreementPageParam inventoryStockAgreementPageParam) throws Exception {
        Page<InventoryStockAgreement> page = new PageInfo<>(inventoryStockAgreementPageParam, OrderItem.desc(getLambdaColumn(InventoryStockAgreement::getCreateTime)));
        LambdaQueryWrapper<InventoryStockAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryStockAgreementPageParam.getStatus()), InventoryStockAgreement::getStatus,inventoryStockAgreementPageParam.getStatus());
        wrapper.eq(StrUtil.isNotBlank(inventoryStockAgreementPageParam.getApprover()), InventoryStockAgreement::getApprover,inventoryStockAgreementPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryStockAgreementPageParam.getCashier()), InventoryStockAgreement::getCashier,inventoryStockAgreementPageParam.getCashier());
        IPage<InventoryStockAgreement> iPage = inventoryStockAgreementMapper.selectPage(page, wrapper);
        iPage.setTotal(inventoryStockAgreementMapper.selectCount(wrapper));
        return new Paging<InventoryStockAgreement>(iPage);
    }
    @Override
    public List<InventoryStockAgreement> getInventoryStockAgreementList(InventoryStockAgreementPageParam inventoryStockAgreementPageParam) throws Exception {
        LambdaQueryWrapper<InventoryStockAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryStockAgreementPageParam.getStatus()), InventoryStockAgreement::getStatus,inventoryStockAgreementPageParam.getStatus());
        wrapper.eq(StrUtil.isNotBlank(inventoryStockAgreementPageParam.getApprover()), InventoryStockAgreement::getApprover,inventoryStockAgreementPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryStockAgreementPageParam.getCashier()), InventoryStockAgreement::getCashier,inventoryStockAgreementPageParam.getCashier());
        List<InventoryStockAgreement> dataLists = inventoryStockAgreementMapper.selectList(wrapper);
        return dataLists;
    }



}
