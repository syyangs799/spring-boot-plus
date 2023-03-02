package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.param.InventoryDailyBusinessPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

/**
 * 日常收入与支出交易流水表 服务类
 *
 * @author syyang
 * @since 2023-03-01
 */
public interface InventoryDailyBusinessService extends BaseService<InventoryDailyBusiness> {

    /**
     * 保存
     *
     * @param inventoryDailyBusiness
     * @return
     * @throws Exception
     */
    boolean saveInventoryDailyBusiness(InventoryDailyBusiness inventoryDailyBusiness) throws Exception;

    /**
     * 修改
     *
     * @param inventoryDailyBusiness
     * @return
     * @throws Exception
     */
    boolean updateInventoryDailyBusiness(InventoryDailyBusiness inventoryDailyBusiness) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryDailyBusiness(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryDailyBusinessQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryDailyBusiness> getInventoryDailyBusinessPageList(InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) throws Exception;

}
