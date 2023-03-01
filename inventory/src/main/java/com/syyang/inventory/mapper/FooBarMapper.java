package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.FooBar;
import com.syyang.inventory.param.FooBarPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * FooBar Mapper 接口
 *
 * @author syyang
 * @since 2023-02-26
 */
@Repository
public interface FooBarMapper extends BaseMapper<FooBar> {


}
