package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.param.InventoryDailyBusinessPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 日常收入与支出交易流水表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-02
 */
@Repository
@ProjectDataPermission
public interface InventoryDailyBusinessMapper extends BaseMapper<InventoryDailyBusiness> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    <E extends IPage<InventoryDailyBusiness>> E selectPage(E page, @Param("ew") Wrapper<InventoryDailyBusiness> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryDailyBusiness> selectList(@Param("ew") Wrapper<InventoryDailyBusiness> queryWrapper);
}
