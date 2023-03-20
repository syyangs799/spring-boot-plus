package com.syyang.inventory.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.enums.ProjectOperationTypeEnum;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.mapper.InventoryProjectBusinessMapper;
import com.syyang.inventory.mapper.InventoryProjectOperationRecordMapper;
import com.syyang.inventory.service.InventoryProjectBusinessService;
import com.syyang.inventory.param.InventoryProjectBusinessPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.syyang.inventory.service.InventoryProjectInfoService;
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

    @Autowired
    private InventoryProjectInfoService inventoryProjectInfoService;
    @Autowired
    private InventoryProjectOperationRecordMapper inventoryProjectOperationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        boolean save = super.save(inventoryProjectBusiness);

        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectBusiness.getProId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) + "],时间[" + DateTime.now().toString("yyyy-MM-DD HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_CREATE_BASE_INFO.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_CREATE_BUSINESS_INFO.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_CREATE_BUSINESS_INFO.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(new InventoryProjectBusiness(),inventoryProjectBusiness));
        inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        InventoryProjectBusiness old = getById(inventoryProjectBusiness.getId());
        boolean b = super.updateById(inventoryProjectBusiness);
        //判断一下如果是出纳或者审核通过 需要更新项目信息
        if(inventoryProjectBusiness.getStatus().equals(String.valueOf(StatusTypeEnum.CASHI_SUCCESS.getCode())) || inventoryProjectBusiness.getStatus().equals(String.valueOf(StatusTypeEnum.CHECK_SUCCESS.getCode()))) {
            inventoryProjectInfoService.calculateProjectInformation(inventoryProjectBusiness.getProId());
        }
        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectBusiness.getProId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) +  "],时间[" + DateTime.now().toString("yyyy-MM-DD HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_UPDATE_BUSINESS_INFO.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_UPDATE_BUSINESS_INFO.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_UPDATE_BUSINESS_INFO.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(old,inventoryProjectBusiness));
        inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        return b;
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
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getStatus()), InventoryProjectBusiness::getStatus,inventoryProjectBusinessPageParam.getStatus());
        wrapper.eq(null != inventoryProjectBusinessPageParam.getProjectId(), InventoryProjectBusiness::getProId,inventoryProjectBusinessPageParam.getProjectId());
        IPage<InventoryProjectBusiness> iPage = inventoryProjectBusinessMapper.selectPage(page, wrapper);
        return new Paging<InventoryProjectBusiness>(iPage);
    }

    @Override
    public List<InventoryProjectBusiness> getInventoryProjectBusinessList(InventoryProjectBusinessPageParam inventoryProjectBusinessPageParam) {
        LambdaQueryWrapper<InventoryProjectBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getStatus()), InventoryProjectBusiness::getStatus,inventoryProjectBusinessPageParam.getStatus());
        wrapper.eq(null != inventoryProjectBusinessPageParam.getProjectId(), InventoryProjectBusiness::getProId,inventoryProjectBusinessPageParam.getProjectId());
        return inventoryProjectBusinessMapper.selectList(wrapper);
    }

}
