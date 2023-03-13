package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryStockInfo;
import com.syyang.inventory.param.InventoryStockInfoPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;

/**
 * 库存信息表 服务类
 *
 * @author syyang
 * @since 2023-03-02
 */
public interface InventoryStockInfoService extends BaseService<InventoryStockInfo> {

    /**
     * 保存
     *
     * @param inventoryStockInfo
     * @return
     * @throws Exception
     */
    boolean saveInventoryStockInfo(InventoryStockInfo inventoryStockInfo) throws Exception;

    /**
     * 修改
     *
     * @param inventoryStockInfo
     * @return
     * @throws Exception
     */
    boolean updateInventoryStockInfo(InventoryStockInfo inventoryStockInfo) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryStockInfo(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryStockInfoQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryStockInfo> getInventoryStockInfoPageList(InventoryStockInfoPageParam inventoryStockInfoPageParam) throws Exception;

    List<InventoryStockInfo> getInventoryStockInfoList(InventoryStockInfoPageParam inventoryStockInfoPageParam);
}
