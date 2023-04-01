package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryDailyType;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.param.InventoryDailyTypePageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 日常支出类型码表 Mapper 接口
 *
 * @author syyang
 * @since 2023-04-01
 */
@Repository
@ProjectDataPermission
public interface InventoryDailyTypeMapper extends BaseMapper<InventoryDailyType> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    Integer selectCount(@Param("ew") Wrapper<InventoryDailyType> queryWrapper);
    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    <E extends IPage<InventoryDailyType>> E selectPage(E page, @Param("ew") Wrapper<InventoryDailyType> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryDailyType> selectList(@Param("ew") Wrapper<InventoryDailyType> queryWrapper);
}
