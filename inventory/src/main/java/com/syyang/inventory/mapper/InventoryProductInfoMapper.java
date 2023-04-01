package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.param.InventoryProductInfoPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 产品信息表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-02
 */
@Repository
@ProjectDataPermission
public interface InventoryProductInfoMapper extends BaseMapper<InventoryProductInfo> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    Integer selectCount(@Param("ew") Wrapper<InventoryProductInfo> queryWrapper);
    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    <E extends IPage<InventoryProductInfo>> E selectPage(E page, @Param("ew") Wrapper<InventoryProductInfo> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryProductInfo> selectList(@Param("ew") Wrapper<InventoryProductInfo> queryWrapper);

}
