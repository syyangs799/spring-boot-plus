package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.param.InventoryProjectBusinessPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

/**
 * 项目收入与支出交易流水表 服务类
 *
 * @author syyang
 * @since 2023-03-02
 */
public interface InventoryProjectBusinessService extends BaseService<InventoryProjectBusiness> {

    /**
     * 保存
     *
     * @param inventoryProjectBusiness
     * @return
     * @throws Exception
     */
    boolean saveInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception;

    /**
     * 修改
     *
     * @param inventoryProjectBusiness
     * @return
     * @throws Exception
     */
    boolean updateInventoryProjectBusiness(InventoryProjectBusiness inventoryProjectBusiness) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryProjectBusiness(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryProjectBusinessQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryProjectBusiness> getInventoryProjectBusinessPageList(InventoryProjectBusinessPageParam inventoryProjectBusinessPageParam) throws Exception;

}
