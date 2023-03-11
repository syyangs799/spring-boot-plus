package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.param.InventoryProjectOperationRecordPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 项目修改记录表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-11
 */
@Repository
public interface InventoryProjectOperationRecordMapper extends BaseMapper<InventoryProjectOperationRecord> {


}
