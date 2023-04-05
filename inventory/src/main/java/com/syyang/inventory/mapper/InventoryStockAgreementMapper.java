package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.entity.InventoryStockAgreement;
import com.syyang.inventory.param.InventoryStockAgreementPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 库存合同表 Mapper 接口
 *
 * @author syyang
 * @since 2023-04-05
 */
@Repository
public interface InventoryStockAgreementMapper extends BaseMapper<InventoryStockAgreement> {


    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    Integer selectCount(@Param("ew") Wrapper<InventoryStockAgreement> queryWrapper);
    @ProjectDataPermission(isCreateUserPermi = false, isDepartmentPermi = true)
    <E extends IPage<InventoryStockAgreement>> E selectPage(E page, @Param("ew") Wrapper<InventoryStockAgreement> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryStockAgreement> selectList(@Param("ew") Wrapper<InventoryStockAgreement> queryWrapper);
}
