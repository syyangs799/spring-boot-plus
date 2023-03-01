package com.syyang.inventory.service;

import com.syyang.inventory.entity.FooBar;
import com.syyang.inventory.param.FooBarPageParam;
import io.geekidea.springbootplus.framework.common.service.BaseService;
import io.geekidea.springbootplus.framework.core.pagination.Paging;

/**
 * FooBar 服务类
 *
 * @author syyang
 * @since 2023-02-26
 */
public interface FooBarService extends BaseService<FooBar> {

    /**
     * 保存
     *
     * @param fooBar
     * @return
     * @throws Exception
     */
    boolean saveFooBar(FooBar fooBar) throws Exception;

    /**
     * 修改
     *
     * @param fooBar
     * @return
     * @throws Exception
     */
    boolean updateFooBar(FooBar fooBar) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteFooBar(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param fooBarQueryParam
     * @return
     * @throws Exception
     */
    Paging<FooBar> getFooBarPageList(FooBarPageParam fooBarPageParam) throws Exception;

}
