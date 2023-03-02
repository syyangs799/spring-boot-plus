package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

/**
 * 产品信息表 服务类
 *
 * @author syyang
 * @since 2023-03-01
 */
public interface InventoryProductInfoService extends BaseService<InventoryProductInfo> {

    /**
     * 保存
     *
     * @param inventoryProductInfo
     * @return
     * @throws Exception
     */
    boolean saveInventoryProductInfo(InventoryProductInfo inventoryProductInfo) throws Exception;

    /**
     * 修改
     *
     * @param inventoryProductInfo
     * @return
     * @throws Exception
     */
    boolean updateInventoryProductInfo(InventoryProductInfo inventoryProductInfo) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryProductInfo(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryProductInfoQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryProductInfo> getInventoryProductInfoPageList(InventoryProductInfoPageParam inventoryProductInfoPageParam) throws Exception;

}
