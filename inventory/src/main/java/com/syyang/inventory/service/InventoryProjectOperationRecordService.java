package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.param.InventoryProjectOperationRecordPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;
/**
 * 项目修改记录表 服务类
 *
 * @author syyang
 * @since 2023-03-11
 */
public interface InventoryProjectOperationRecordService extends BaseService<InventoryProjectOperationRecord> {

    /**
     * 保存
     *
     * @param inventoryProjectOperationRecord
     * @return
     * @throws Exception
     */
    boolean saveInventoryProjectOperationRecord(InventoryProjectOperationRecord inventoryProjectOperationRecord) throws Exception;

    /**
     * 修改
     *
     * @param inventoryProjectOperationRecord
     * @return
     * @throws Exception
     */
    boolean updateInventoryProjectOperationRecord(InventoryProjectOperationRecord inventoryProjectOperationRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryProjectOperationRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryProjectOperationRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryProjectOperationRecord> getInventoryProjectOperationRecordPageList(InventoryProjectOperationRecordPageParam inventoryProjectOperationRecordPageParam) throws Exception;


    /**
     * 获取列表对象
     *
     * @param inventoryProjectOperationRecordQueryParam
     * @return
     * @throws Exception
     */
    List<InventoryProjectOperationRecord> getInventoryProjectOperationRecordList(InventoryProjectOperationRecordPageParam inventoryProjectOperationRecordPageParam) throws Exception;

}
