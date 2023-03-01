package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.FooBar;
import com.syyang.inventory.mapper.FooBarMapper;
import com.syyang.inventory.service.FooBarService;
import com.syyang.inventory.param.FooBarPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.geekidea.springbootplus.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.springbootplus.framework.core.pagination.Paging;
import io.geekidea.springbootplus.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FooBar 服务实现类
 *
 * @author syyang
 * @since 2023-02-26
 */
@Slf4j
@Service
public class FooBarServiceImpl extends BaseServiceImpl<FooBarMapper, FooBar> implements FooBarService {

    @Autowired
    private FooBarMapper fooBarMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveFooBar(FooBar fooBar) throws Exception {
        return super.save(fooBar);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateFooBar(FooBar fooBar) throws Exception {
        return super.updateById(fooBar);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteFooBar(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<FooBar> getFooBarPageList(FooBarPageParam fooBarPageParam) throws Exception {
        Page<FooBar> page = new PageInfo<>(fooBarPageParam, OrderItem.desc(getLambdaColumn(FooBar::getCreateTime)));
        LambdaQueryWrapper<FooBar> wrapper = new LambdaQueryWrapper<>();
        IPage<FooBar> iPage = fooBarMapper.selectPage(page, wrapper);
        return new Paging<FooBar>(iPage);
    }

}
