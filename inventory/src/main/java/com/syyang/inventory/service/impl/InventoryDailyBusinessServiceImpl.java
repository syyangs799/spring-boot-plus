package com.syyang.inventory.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryStockInfo;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.mapper.InventoryDailyBusinessMapper;
import com.syyang.inventory.service.InventoryDailyBusinessService;
import com.syyang.inventory.param.InventoryDailyBusinessPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 日常收入与支出交易流水表 服务实现类
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@Service
public class InventoryDailyBusinessServiceImpl extends BaseServiceImpl<InventoryDailyBusinessMapper, InventoryDailyBusiness> implements InventoryDailyBusinessService {

    @Autowired
    private InventoryDailyBusinessMapper inventoryDailyBusinessMapper;
    @Autowired
    private LoginRedisService loginRedisService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInventoryDailyBusiness(InventoryDailyBusiness inventoryDailyBusiness) throws Exception {
        return super.save(inventoryDailyBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInventoryDailyBusiness(InventoryDailyBusiness inventoryDailyBusiness) throws Exception {
        return super.updateById(inventoryDailyBusiness);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInventoryDailyBusiness(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InventoryDailyBusiness> getInventoryDailyBusinessPageList(InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) throws Exception {
        Page<InventoryDailyBusiness> page = new PageInfo<>(inventoryDailyBusinessPageParam, OrderItem.desc(getLambdaColumn(InventoryDailyBusiness::getCreateTime)));
        LambdaQueryWrapper<InventoryDailyBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getStatus()),InventoryDailyBusiness::getStatus,inventoryDailyBusinessPageParam.getStatus());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getApprover()),InventoryDailyBusiness::getApprover,inventoryDailyBusinessPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getCashier()),InventoryDailyBusiness::getCashier,inventoryDailyBusinessPageParam.getCashier());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getSubType()),InventoryDailyBusiness::getSubType,inventoryDailyBusinessPageParam.getSubType());
        IPage<InventoryDailyBusiness> iPage = inventoryDailyBusinessMapper.selectPage(page, wrapper);
        iPage.setTotal(inventoryDailyBusinessMapper.selectCount(wrapper));
        return new Paging<InventoryDailyBusiness>(iPage);
    }

    @Override
    public List<InventoryDailyBusiness> getInventoryDailyBusinessList(InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) {
        LambdaQueryWrapper<InventoryDailyBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getStatus()),InventoryDailyBusiness::getStatus,inventoryDailyBusinessPageParam.getStatus());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getApprover()),InventoryDailyBusiness::getApprover,inventoryDailyBusinessPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getCashier()),InventoryDailyBusiness::getCashier,inventoryDailyBusinessPageParam.getCashier());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getSubType()),InventoryDailyBusiness::getSubType,inventoryDailyBusinessPageParam.getSubType());
        return inventoryDailyBusinessMapper.selectList(wrapper);
    }

    @Override
    public List<KeyAndValueVo> getDailyBusinessAmount(InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) {
        LoginSysUserRedisVo loginSysUserRedisVo = loginRedisService.getLoginSysUserRedisVo(JwtUtil.getUsername(JwtTokenUtil.getToken()));
        List<KeyAndValueVo> keyAndValueVos = Lists.newArrayList();
        LambdaQueryWrapper<InventoryDailyBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getStatus()),InventoryDailyBusiness::getStatus,inventoryDailyBusinessPageParam.getStatus());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getApprover()),InventoryDailyBusiness::getApprover,inventoryDailyBusinessPageParam.getApprover());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getCashier()),InventoryDailyBusiness::getCashier,inventoryDailyBusinessPageParam.getCashier());
        wrapper.eq(StrUtil.isNotBlank(inventoryDailyBusinessPageParam.getSubType()),InventoryDailyBusiness::getSubType,inventoryDailyBusinessPageParam.getSubType());
        List<InventoryDailyBusiness> inventoryDailyBusinesses = inventoryDailyBusinessMapper.selectList(wrapper);
        Map<String,BigDecimal> outAmountMap = Maps.newConcurrentMap();
        BigDecimal totalAmount = new BigDecimal(0);


        BigDecimal approveAmount = new BigDecimal(0);
        BigDecimal cashierAmount = new BigDecimal(0);
        BigDecimal noCashierAmount = new BigDecimal(0);

        for (InventoryDailyBusiness inventoryDailyBusiness : inventoryDailyBusinesses) {
            BigDecimal count = outAmountMap.getOrDefault(inventoryDailyBusiness.getSubTypeName(),
                    new BigDecimal("0")).add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getAmountMoney())));
            outAmountMap.put(inventoryDailyBusiness.getSubTypeName(), count);
            totalAmount = totalAmount.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getAmountMoney())));
            if(loginSysUserRedisVo.getRoleCode().equals("adminD") && inventoryDailyBusiness.getCreateUser().equals(loginSysUserRedisVo.getId().toString())){
                //统计个人的统计信息
                if(inventoryDailyBusiness.getStatus().equals(StatusTypeEnum.CHECK_SUCCESS.getCode().toString())
                        || inventoryDailyBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString()) ){
                    approveAmount = approveAmount.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getAmountMoney())));
                    if(inventoryDailyBusiness.getStatus().equals(StatusTypeEnum.CASHI_SUCCESS.getCode().toString())){
                        cashierAmount = cashierAmount.add(BigDecimal.valueOf(Double.valueOf(inventoryDailyBusiness.getCashierAmount())));
                    }
                }
            }
        }
        //去掉其他类型的总金额统计
//        for(Map.Entry<String,BigDecimal> entry:outAmountMap.entrySet()) {
//            keyAndValueVos.add(new KeyAndValueVo(entry.getKey(), entry.getValue().divide(BigDecimal.valueOf(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
//        }
        noCashierAmount = approveAmount.subtract(cashierAmount);
        keyAndValueVos.add(new KeyAndValueVo("日常支出总金额", totalAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));
        if(loginSysUserRedisVo.getRoleCode().equals("adminD")){
            keyAndValueVos.add(new KeyAndValueVo("个人已通过总金额", approveAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));
            keyAndValueVos.add(new KeyAndValueVo("个人已报销总金额", cashierAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));
            keyAndValueVos.add(new KeyAndValueVo("个人未报销总金额", noCashierAmount.divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString() + "万元"));

        }
        return keyAndValueVos;
    }

}
