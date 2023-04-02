package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryDailyType;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.entity.InventoryProductType;
import com.syyang.inventory.mapper.InventoryProductInfoMapper;
import com.syyang.inventory.mapper.InventoryProductTypeMapper;
import com.syyang.inventory.service.InventoryProductTypeService;
import com.syyang.inventory.param.InventoryProductTypePageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.syyang.springbootplus.framework.common.exception.BusinessException;
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
    @Autowired
    private InventoryProductInfoMapper inventoryProductInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProductType(InventoryProductType inventoryProductType) throws Exception {
        valideUniName(inventoryProductType);
        return super.save(inventoryProductType);
    }

    private void valideUniName(InventoryProductType inventoryProductType) throws Exception {
        LambdaQueryWrapper<InventoryProductType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryProductType::getProductName,inventoryProductType.getProductName());
        wrapper.ne(null != inventoryProductType.getId(),InventoryProductType::getId,inventoryProductType.getId());
        List<InventoryProductType> dataLists = inventoryProductTypeMapper.selectList(wrapper);
        if(dataLists.size() > 0){
            throw new BusinessException("当前已存在名称【" + inventoryProductType.getProductName() + "】的产品类型，请重新输入");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProductType(InventoryProductType inventoryProductType) throws Exception {
        valideUniName(inventoryProductType);
        return super.updateById(inventoryProductType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryProductType(Long id) throws Exception {
        //判断是否已经存在
        LambdaQueryWrapper<InventoryProductInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryProductInfo::getProductType,id.toString());
        List<InventoryProductInfo> inventoryDailyBusinesses = inventoryProductInfoMapper.selectList(wrapper);
        if(inventoryDailyBusinesses.size()>0){
            throw new BusinessException("当前产品类型已存在相应的产品信息，无法删除！");
        }
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
