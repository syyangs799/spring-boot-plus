package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryProductType;
import com.syyang.inventory.param.InventoryProductTypePageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;
/**
 * 产品类型码表 服务类
 *
 * @author syyang
 * @since 2023-04-01
 */
public interface InventoryProductTypeService extends BaseService<InventoryProductType> {

    /**
     * 保存
     *
     * @param inventoryProductType
     * @return
     * @throws Exception
     */
    boolean saveInventoryProductType(InventoryProductType inventoryProductType) throws Exception;

    /**
     * 修改
     *
     * @param inventoryProductType
     * @return
     * @throws Exception
     */
    boolean updateInventoryProductType(InventoryProductType inventoryProductType) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryProductType(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryProductTypeQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryProductType> getInventoryProductTypePageList(InventoryProductTypePageParam inventoryProductTypePageParam) throws Exception;


    /**
     * 获取列表对象
     *
     * @param inventoryProductTypeQueryParam
     * @return
     * @throws Exception
     */
    List<InventoryProductType> getInventoryProductTypeList(InventoryProductTypePageParam inventoryProductTypePageParam) throws Exception;

}
