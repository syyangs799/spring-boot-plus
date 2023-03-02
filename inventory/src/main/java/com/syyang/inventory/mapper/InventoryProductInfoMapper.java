package com.syyang.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.param.InventoryProductInfoPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 产品信息表 Mapper 接口
 *
 * @author syyang
 * @since 2023-03-01
 */
@Repository
public interface InventoryProductInfoMapper extends BaseMapper<InventoryProductInfo> {


}
