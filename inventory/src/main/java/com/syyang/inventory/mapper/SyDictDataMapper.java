package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.SyDictData;
import com.syyang.inventory.param.SyDictDataPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 字典数据表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-11
 */
@Repository
public interface SyDictDataMapper extends BaseMapper<SyDictData> {


}
