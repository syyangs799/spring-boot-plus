package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.param.InventoryProjectOperationRecordPageParam;

import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 项目修改记录表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-12
 */
@Repository
@ProjectDataPermission
public interface InventoryProjectOperationRecordMapper extends BaseMapper<InventoryProjectOperationRecord> {


    @ProjectDataPermission(isCreateUserPermi = false, isDepartmentPermi = true)
    <E extends IPage<InventoryProjectOperationRecord>> E selectPage(E page, @Param("ew") Wrapper<InventoryProjectOperationRecord> queryWrapper);

    @ProjectDataPermission(isCreateUserPermi = false,isDepartmentPermi = true)
    List<InventoryProjectOperationRecord> selectList(@Param("ew") Wrapper<InventoryProjectOperationRecord> queryWrapper);
}
