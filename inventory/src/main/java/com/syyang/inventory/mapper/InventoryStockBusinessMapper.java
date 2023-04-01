package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryStockBusiness;
import com.syyang.inventory.entity.InventoryStockInfo;
import com.syyang.inventory.param.InventoryStockBusinessPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 库存交易流水表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-02
 */
@Repository
@ProjectDataPermission
public interface InventoryStockBusinessMapper extends BaseMapper<InventoryStockBusiness> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    Integer selectCount(@Param("ew") Wrapper<InventoryStockBusiness> queryWrapper);
    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    <E extends IPage<InventoryStockBusiness>> E selectPage(E page, @Param("ew") Wrapper<InventoryStockBusiness> queryWrapper);


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryStockBusiness> selectList(@Param("ew") Wrapper<InventoryStockBusiness> queryWrapper);
}
