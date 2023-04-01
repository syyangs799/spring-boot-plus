package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.param.InventoryProjectBusinessPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 项目收入与支出交易流水表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-02
 */
@Repository
@ProjectDataPermission
public interface InventoryProjectBusinessMapper extends BaseMapper<InventoryProjectBusiness> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    Integer selectCount(@Param("ew") Wrapper<InventoryProjectBusiness> queryWrapper);
    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    <E extends IPage<InventoryProjectBusiness>> E selectPage(E page, @Param("ew") Wrapper<InventoryProjectBusiness> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryProjectBusiness> selectList(@Param("ew") Wrapper<InventoryProjectBusiness> queryWrapper);
}
