package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.InventoryStockBusiness;
import com.syyang.inventory.entity.vo.CompanyCashierVo;
import com.syyang.inventory.param.InventoryOverviewParam;
import com.syyang.inventory.param.InventoryProjectInfoPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 项目信息表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-02
 */
@Repository
@ProjectDataPermission
public interface InventoryProjectInfoMapper extends BaseMapper<InventoryProjectInfo> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    Integer selectCount(@Param("ew") Wrapper<InventoryProjectInfo> queryWrapper);
    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    <E extends IPage<InventoryProjectInfo>> E selectPage(E page, @Param("ew") Wrapper<InventoryProjectInfo> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryProjectInfo> selectList(@Param("ew") Wrapper<InventoryProjectInfo> queryWrapper);


    List<CompanyCashierVo> getCompanyCashierByDates(@Param("inventoryOverviewParam") InventoryOverviewParam inventoryOverviewParam,@Param("departmentId") Long departmentId);
}
