package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryDailyType;
import com.syyang.inventory.param.InventoryDailyTypePageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;
/**
 * 日常支出类型码表 服务类
 *
 * @author syyang
 * @since 2023-04-01
 */
public interface InventoryDailyTypeService extends BaseService<InventoryDailyType> {

    /**
     * 保存
     *
     * @param inventoryDailyType
     * @return
     * @throws Exception
     */
    boolean saveInventoryDailyType(InventoryDailyType inventoryDailyType) throws Exception;

    /**
     * 修改
     *
     * @param inventoryDailyType
     * @return
     * @throws Exception
     */
    boolean updateInventoryDailyType(InventoryDailyType inventoryDailyType) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryDailyType(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryDailyTypeQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryDailyType> getInventoryDailyTypePageList(InventoryDailyTypePageParam inventoryDailyTypePageParam) throws Exception;


    /**
     * 获取列表对象
     *
     * @param inventoryDailyTypeQueryParam
     * @return
     * @throws Exception
     */
    List<InventoryDailyType> getInventoryDailyTypeList(InventoryDailyTypePageParam inventoryDailyTypePageParam) throws Exception;

}
