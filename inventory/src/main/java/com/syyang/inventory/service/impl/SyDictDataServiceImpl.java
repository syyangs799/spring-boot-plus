package com.syyang.inventory.service.impl;

import com.syyang.inventory.entity.SyDictData;
import com.syyang.inventory.mapper.SyDictDataMapper;
import com.syyang.inventory.service.SyDictDataService;
import com.syyang.inventory.param.SyDictDataPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.syyang.springbootplus.framework.common.service.impl.BaseServiceImpl;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/**
 * 字典数据表 服务实现类
 *
 * @author syyang
 * @since 2023-03-11
 */
@Slf4j
@Service
public class SyDictDataServiceImpl extends BaseServiceImpl<SyDictDataMapper, SyDictData> implements SyDictDataService {

    @Autowired
    private SyDictDataMapper syDictDataMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSyDictData(SyDictData syDictData) throws Exception {
        return super.save(syDictData);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSyDictData(SyDictData syDictData) throws Exception {
        return super.updateById(syDictData);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSyDictData(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<SyDictData> getSyDictDataPageList(SyDictDataPageParam syDictDataPageParam) throws Exception {
        Page<SyDictData> page = new PageInfo<>(syDictDataPageParam, OrderItem.desc(getLambdaColumn(SyDictData::getCreateTime)));
        LambdaQueryWrapper<SyDictData> wrapper = new LambdaQueryWrapper<>();
        IPage<SyDictData> iPage = syDictDataMapper.selectPage(page, wrapper);
        return new Paging<SyDictData>(iPage);
    }
    @Override
    public List<SyDictData> getSyDictDataList(SyDictDataPageParam syDictDataPageParam) throws Exception {
        LambdaQueryWrapper<SyDictData> wrapper = new LambdaQueryWrapper<>();
        List<SyDictData> dataLists = syDictDataMapper.selectList(wrapper);
        return dataLists;
    }



}
