package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryProjectOtherBusiness;
import com.syyang.inventory.param.InventoryProjectOtherBusinessPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;
/**
 * 项目其他支出信息表 服务类
 *
 * @author syyang
 * @since 2023-04-05
 */
public interface InventoryProjectOtherBusinessService extends BaseService<InventoryProjectOtherBusiness> {

    /**
     * 保存
     *
     * @param inventoryProjectOtherBusiness
     * @return
     * @throws Exception
     */
    boolean saveInventoryProjectOtherBusiness(InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception;

    /**
     * 修改
     *
     * @param inventoryProjectOtherBusiness
     * @return
     * @throws Exception
     */
    boolean updateInventoryProjectOtherBusiness(InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryProjectOtherBusiness(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryProjectOtherBusinessQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryProjectOtherBusiness> getInventoryProjectOtherBusinessPageList(InventoryProjectOtherBusinessPageParam inventoryProjectOtherBusinessPageParam) throws Exception;


    /**
     * 获取列表对象
     *
     * @param inventoryProjectOtherBusinessQueryParam
     * @return
     * @throws Exception
     */
    List<InventoryProjectOtherBusiness> getInventoryProjectOtherBusinessList(InventoryProjectOtherBusinessPageParam inventoryProjectOtherBusinessPageParam) throws Exception;

}
