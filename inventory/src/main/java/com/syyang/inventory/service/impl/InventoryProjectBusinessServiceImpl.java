package com.syyang.inventory.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.syyang.inventory.entity.*;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.enums.ProjectOperationTypeEnum;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.enums.StockBusinessTypeEnum;
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
import com.syyang.springbootplus.framework.shiro.cache.LoginRedisService;
import com.syyang.springbootplus.framework.shiro.util.JwtTokenUtil;
import com.syyang.springbootplus.framework.shiro.util.JwtUtil;
import com.syyang.springbootplus.framework.shiro.vo.LoginSysUserRedisVo;
import com.syyang.springbootplus.framework.util.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

//    @Autowired
//    private InventoryProjectInfoService inventoryProjectInfoService;
    @Autowired
    private InventoryProjectOperationRecordMapper inventoryProjectOperationRecordMapper;

    @Autowired
    private LoginRedisService loginRedisService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        boolean save = super.save(inventoryProjectBusiness);

        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectBusiness.getProId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) + "],时间[" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_CREATE_BASE_INFO.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_CREATE_BUSINESS_INFO.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_CREATE_BUSINESS_INFO.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(new InventoryProjectBusiness(),inventoryProjectBusiness));
        if(StrUtil.isNotBlank(inventoryProjectOperationRecord.getUpdateContent())) {
            inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        }
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        InventoryProjectBusiness old = getById(inventoryProjectBusiness.getId());
        boolean b = super.updateById(inventoryProjectBusiness);

        //添加项目操作日志 谁创建了项目
        InventoryProjectOperationRecord inventoryProjectOperationRecord = new InventoryProjectOperationRecord();
        inventoryProjectOperationRecord.setProjectId(inventoryProjectBusiness.getProId());
        //谁+时间+操作类型+内容
        inventoryProjectOperationRecord.setOperationName("人员[" + JwtUtil.getUsername(JwtTokenUtil.getToken()) +  "],时间[" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "],操作[" + ProjectOperationTypeEnum.PROJECT_UPDATE_BUSINESS_INFO.getDesc() + "]");
        inventoryProjectOperationRecord.setOperationType(ProjectOperationTypeEnum.PROJECT_UPDATE_BUSINESS_INFO.getDesc());
        inventoryProjectOperationRecord.setOperationTypeName(ProjectOperationTypeEnum.PROJECT_UPDATE_BUSINESS_INFO.getCode().toString());
        inventoryProjectOperationRecord.setUpdateContent(BeanUtils.getChangedFields(old,inventoryProjectBusiness));
        if(StrUtil.isNotBlank(inventoryProjectOperationRecord.getUpdateContent())) {
            inventoryProjectOperationRecordMapper.insert(inventoryProjectOperationRecord);
        }
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
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getApprover()), InventoryProjectBusiness::getApprover,inventoryProjectBusinessPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getCashier()), InventoryProjectBusiness::getCashier,inventoryProjectBusinessPageParam.getCashier());
        wrapper.eq(null != inventoryProjectBusinessPageParam.getProjectId(), InventoryProjectBusiness::getProId,inventoryProjectBusinessPageParam.getProjectId());
        IPage<InventoryProjectBusiness> iPage = inventoryProjectBusinessMapper.selectPage(page, wrapper);
        iPage.setTotal(inventoryProjectBusinessMapper.selectCount(wrapper));
        return new Paging<InventoryProjectBusiness>(iPage);
    }

    @Override
    public List<InventoryProjectBusiness> getInventoryProjectBusinessList(InventoryProjectBusinessPageParam inventoryProjectBusinessPageParam) {
        LambdaQueryWrapper<InventoryProjectBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getStatus()), InventoryProjectBusiness::getStatus,inventoryProjectBusinessPageParam.getStatus());
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getApprover()), InventoryProjectBusiness::getApprover,inventoryProjectBusinessPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getCashier()), InventoryProjectBusiness::getCashier,inventoryProjectBusinessPageParam.getCashier());
        wrapper.eq(null != inventoryProjectBusinessPageParam.getProjectId(), InventoryProjectBusiness::getProId,inventoryProjectBusinessPageParam.getProjectId());
        return inventoryProjectBusinessMapper.selectList(wrapper);
    }

    @Override
    public List<KeyAndValueVo> getProjectBusinessAmount(InventoryProjectBusinessPageParam inventoryProjectBusinessPageParam) {
        LoginSysUserRedisVo loginSysUserRedisVo = loginRedisService.getLoginSysUserRedisVo(JwtUtil.getUsername(JwtTokenUtil.getToken()));
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        LambdaQueryWrapper<InventoryProjectBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getStatus()), InventoryProjectBusiness::getStatus,inventoryProjectBusinessPageParam.getStatus());
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getApprover()), InventoryProjectBusiness::getApprover,inventoryProjectBusinessPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryProjectBusinessPageParam.getCashier()), InventoryProjectBusiness::getCashier,inventoryProjectBusinessPageParam.getCashier());
        wrapper.eq(null != inventoryProjectBusinessPageParam.getProjectId(), InventoryProjectBusiness::getProId,inventoryProjectBusinessPageParam.getProjectId());
        List<InventoryProjectBusiness> inventoryProjectBusinesses = inventoryProjectBusinessMapper.selectList(wrapper);
        Map<String,BigDecimal> inAmountMap = Maps.newConcurrentMap();
        Map<String,BigDecimal> outAmountMap = Maps.newConcurrentMap();
        BigDecimal inTotalAmount = new BigDecimal(0);
        BigDecimal outTotalAmount = new BigDecimal(0);


        BigDecimal approveAmount = new BigDecimal(0);
        BigDecimal cashierAmount = new BigDecimal(0);
        BigDecimal noCashierAmount = new BigDecimal(0);

        for (InventoryProjectBusiness inventoryProjectBusiness : inventoryProjectBusinesses) {
            if(inventoryProjectBusiness.getType().equals(StockBusinessTypeEnum.IN.getCode())){
                BigDecimal count = outAmountMap.getOrDefault(inventoryProjectBusiness.getSubTypeName(),
                        new BigDecimal("0")).add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
                inAmountMap.put(inventoryProjectBusiness.getSubTypeName(), count);
                inTotalAmount = inTotalAmount.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
            }else{
                BigDecimal count = outAmountMap.getOrDefault(inventoryProjectBusiness.getSubTypeName(),
                        new BigDecimal("0")).add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
                outAmountMap.put(inventoryProjectBusiness.getSubTypeName(), count);
                outTotalAmount = outTotalAmount.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
            }
            if(loginSysUserRedisVo.getRoleCode().equals("adminD") && inventoryProjectBusiness.getCreateUser().equals(loginSysUserRedisVo.getId().toString())){
                //统计个人的统计信息
                if(inventoryProjectBusiness.getStatus().equals(StatusTypeEnum.CHECK_SUCCESS.getCode().toString())
                        || inventoryProjectBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString()) ){
                    approveAmount = approveAmount.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getAmountMoney())));
                    if(inventoryProjectBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())){
                        cashierAmount = cashierAmount.add(BigDecimal.valueOf(Double.valueOf(inventoryProjectBusiness.getCashierAmount())));
                    }
                }
            }
        }

//        for(Map.Entry<String,BigDecimal> entry:inAmountMap.entrySet()) {
//            keyAndValueVos.add(new KeyAndValueVo("收入：" + entry.getKey(), entry.getValue().toString()));
//        }
//        for(Map.Entry<String,BigDecimal> entry:outAmountMap.entrySet()) {
//            keyAndValueVos.add(new KeyAndValueVo("支出：" + entry.getKey(), entry.getValue().toString()));
//        }
        noCashierAmount = approveAmount.subtract(cashierAmount);
        keyAndValueVos.add(new KeyAndValueVo("项目收入总金额", inTotalAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));
        keyAndValueVos.add(new KeyAndValueVo("项目支出总金额", outTotalAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));
        if(loginSysUserRedisVo.getRoleCode().equals("adminD")){
            keyAndValueVos.add(new KeyAndValueVo("个人已通过总金额", approveAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));
            keyAndValueVos.add(new KeyAndValueVo("个人已报销总金额", cashierAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));
            keyAndValueVos.add(new KeyAndValueVo("个人未报销总金额", noCashierAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));

        }
        return keyAndValueVos;
    }

}
