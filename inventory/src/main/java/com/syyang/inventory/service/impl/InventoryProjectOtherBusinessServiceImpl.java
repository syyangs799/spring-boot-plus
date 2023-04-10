package com.syyang.inventory.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.syyang.inventory.entity.*;
import com.syyang.inventory.enums.ProjectOperationTypeEnum;
import com.syyang.inventory.mapper.InventoryProjectOperationRecordMapper;
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
import com.syyang.springbootplus.framework.shiro.util.JwtTokenUtil;
import com.syyang.springbootplus.framework.shiro.util.JwtUtil;
import com.syyang.springbootplus.framework.util.BeanUtils;
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

    @Autowired
    private InventoryProjectOperationRecordMapper inventoryProjectOperationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectOtherBusiness(InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception {
        boolean save = super.save(inventoryProjectOtherBusiness);
        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectOtherBusiness.getProjectId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) + "],时间[" + DateTime.now().toString("yyyy-MM-DD HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_CREATE_OTHER_BUSINESS.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_CREATE_OTHER_BUSINESS.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_CREATE_OTHER_BUSINESS.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(new InventoryStockBusiness(),inventoryProjectOtherBusiness));
        if(StrUtil.isNotBlank(inventoryProjectOperationRecord.getUpdateContent())) {
            inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        }
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectOtherBusiness(InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception {
        InventoryProjectOtherBusiness old = getById(inventoryProjectOtherBusiness.getId());
        boolean b = super.updateById(inventoryProjectOtherBusiness);
        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectOtherBusiness.getProjectId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) +  "],时间[" + DateTime.now().toString("yyyy-MM-DD HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_UPDATE_OTHER_BUSINESS.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_UPDATE_OTHER_BUSINESS.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_UPDATE_OTHER_BUSINESS.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(old,inventoryProjectOtherBusiness));
        if(StrUtil.isNotBlank(inventoryProjectOperationRecord.getUpdateContent())) {
            inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        }
        return b;
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
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectOtherBusinessPageParam.getStatus()), InventoryProjectOtherBusiness::getStatus,inventoryProjectOtherBusinessPageParam.getStatus());
        wrapper.eq(null != inventoryProjectOtherBusinessPageParam.getProjectId(), InventoryProjectOtherBusiness::getProjectId,inventoryProjectOtherBusinessPageParam.getProjectId());
        IPage<InventoryProjectOtherBusiness> iPage = inventoryProjectOtherBusinessMapper.selectPage(page, wrapper);
        iPage.setTotal(inventoryProjectOtherBusinessMapper.selectCount(wrapper));
        return new Paging<InventoryProjectOtherBusiness>(iPage);
    }
    @Override
    public List<InventoryProjectOtherBusiness> getInventoryProjectOtherBusinessList(InventoryProjectOtherBusinessPageParam inventoryProjectOtherBusinessPageParam) throws Exception {
        LambdaQueryWrapper<InventoryProjectOtherBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectOtherBusinessPageParam.getStatus()), InventoryProjectOtherBusiness::getStatus,inventoryProjectOtherBusinessPageParam.getStatus());
        wrapper.eq(null != inventoryProjectOtherBusinessPageParam.getProjectId(), InventoryProjectOtherBusiness::getProjectId,inventoryProjectOtherBusinessPageParam.getProjectId());
        List<InventoryProjectOtherBusiness> dataLists = inventoryProjectOtherBusinessMapper.selectList(wrapper);
        return dataLists;
    }



}
