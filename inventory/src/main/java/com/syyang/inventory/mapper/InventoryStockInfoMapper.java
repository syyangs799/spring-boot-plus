package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryStockInfo;
import com.syyang.inventory.param.InventoryStockInfoPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 库存信息表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-02
 */
@Repository
public interface InventoryStockInfoMapper extends BaseMapper<InventoryStockInfo> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    <E extends IPage<InventoryStockInfo>> E selectPage(E page, @Param("ew") Wrapper<InventoryStockInfo> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryStockInfo> selectList(@Param("ew") Wrapper<InventoryStockInfo> queryWrapper);

}


