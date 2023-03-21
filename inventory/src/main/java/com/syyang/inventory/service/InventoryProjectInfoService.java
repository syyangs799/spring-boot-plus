package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.param.InventoryOverviewParam;
import com.syyang.inventory.param.InventoryProjectInfoPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;

/**
 * 项目信息表 服务类
 *
 * @author syyang
 * @since 2023-03-02
 */
public interface InventoryProjectInfoService extends BaseService<InventoryProjectInfo> {

    /**
     * 保存
     *
     * @param inventoryProjectInfo
     * @return
     * @throws Exception
     */
    boolean saveInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception;

    /**
     * 修改
     *
     * @param inventoryProjectInfo
     * @return
     * @throws Exception
     */
    boolean updateInventoryProjectInfo(InventoryProjectInfo inventoryProjectInfo) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInventoryProjectInfo(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inventoryProjectInfoQueryParam
     * @return
     * @throws Exception
     */
    Paging<InventoryProjectInfo> getInventoryProjectInfoPageList(InventoryProjectInfoPageParam inventoryProjectInfoPageParam) throws Exception;

    List<InventoryProjectInfo> getInventoryProjectInfoList(InventoryProjectInfoPageParam inventoryProjectInfoPageParam);



    /**
     * 重新计算项目信息--新建后--修改后 自动进行计算
     * @param proId 项目id
     * @return
     */
    boolean calculateProjectInformation(Integer proId);

}
