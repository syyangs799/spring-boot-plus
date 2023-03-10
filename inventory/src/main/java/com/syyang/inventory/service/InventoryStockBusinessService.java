package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryStockBusiness;
import com.syyang.inventory.param.InventoryStockBusinessPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;

/**
 * 库存交易流水表 服务类
 *
 * @author syyang
 * @since 2023-03-02
 */
public interface InventoryStockBusinessService extends BaseService<InventoryStockBusiness> {

    /**
     * 保存
     *
     * @param inventoryStockBusiness
     * @return
     * @throws Exception
     */
    boolean saveInventoryStockBusiness(InventoryStockBusiness inventoryStockBusiness) throws Exception;

    /**
     * 修改
     *
     * @param inventoryStockBusiness
     * @return
     * @throws Exception
     */
    boolean updateInventoryStockBusiness(InventoryStockBusiness inventoryStockBusiness) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryStockBusiness(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryStockBusinessQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryStockBusiness> getInventoryStockBusinessPageList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam) throws Exception;

    boolean addOutStockBusiness(List<InventoryStockBusiness> inventoryStockBusiness) throws Exception ;

    List<InventoryStockBusiness> getInventoryStockBusinessList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam);

    List<InventoryStockBusiness> getInventoryStockBusinessDeliveredList(InventoryStockBusinessPageParam inventoryStockBusinessPageParam);
}
