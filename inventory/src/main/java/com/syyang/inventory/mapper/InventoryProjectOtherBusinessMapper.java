package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.entity.InventoryProjectOtherBusiness;
import com.syyang.inventory.param.InventoryProjectOtherBusinessPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 项目其他支出信息表 Mapper 接口
 *
 * @author syyang
 * @since 2023-04-05
 */
@Repository
@ProjectDataPermission
public interface InventoryProjectOtherBusinessMapper extends BaseMapper<InventoryProjectOtherBusiness> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    Integer selectCount(@Param("ew") Wrapper<InventoryProjectOtherBusiness> queryWrapper);
    @ProjectDataPermission(isCreateUserPermi = false, isDepartmentPermi = true)
    <E extends IPage<InventoryProjectOtherBusiness>> E selectPage(E page, @Param("ew") Wrapper<InventoryProjectOtherBusiness> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryProjectOtherBusiness> selectList(@Param("ew") Wrapper<InventoryProjectOtherBusiness> queryWrapper);
}
