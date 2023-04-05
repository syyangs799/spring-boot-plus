package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryStockAgreement;
import com.syyang.inventory.param.InventoryStockAgreementPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;
/**
 * 库存合同表 服务类
 *
 * @author syyang
 * @since 2023-04-05
 */
public interface InventoryStockAgreementService extends BaseService<InventoryStockAgreement> {

    /**
     * 保存
     *
     * @param inventoryStockAgreement
     * @return
     * @throws Exception
     */
    boolean saveInventoryStockAgreement(InventoryStockAgreement inventoryStockAgreement) throws Exception;

    /**
     * 修改
     *
     * @param inventoryStockAgreement
     * @return
     * @throws Exception
     */
    boolean updateInventoryStockAgreement(InventoryStockAgreement inventoryStockAgreement) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryStockAgreement(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryStockAgreementQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryStockAgreement> getInventoryStockAgreementPageList(InventoryStockAgreementPageParam inventoryStockAgreementPageParam) throws Exception;


    /**
     * 获取列表对象
     *
     * @param inventoryStockAgreementQueryParam
     * @return
     * @throws Exception
     */
    List<InventoryStockAgreement> getInventoryStockAgreementList(InventoryStockAgreementPageParam inventoryStockAgreementPageParam) throws Exception;

}
