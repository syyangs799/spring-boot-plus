package com.syyang.inventory.service;

import com.syyang.inventory.entity.SyDictData;
import com.syyang.inventory.param.SyDictDataPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;
/**
 * 字典数据表 服务类
 *
 * @author syyang
 * @since 2023-03-11
 */
public interface SyDictDataService extends BaseService<SyDictData> {

    /**
     * 保存
     *
     * @param syDictData
     * @return
     * @throws Exception
     */
    boolean saveSyDictData(SyDictData syDictData) throws Exception;

    /**
     * 修改
     *
     * @param syDictData
     * @return
     * @throws Exception
     */
    boolean updateSyDictData(SyDictData syDictData) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSyDictData(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param syDictDataQueryParam
     * @return
     * @throws Exception
     */
    Paging<SyDictData> getSyDictDataPageList(SyDictDataPageParam syDictDataPageParam) throws Exception;


    /**
     * 获取列表对象
     *
     * @param syDictDataQueryParam
     * @return
     * @throws Exception
     */
    List<SyDictData> getSyDictDataList(SyDictDataPageParam syDictDataPageParam) throws Exception;

}
