package com.syyang.inventory.controller;

import com.syyang.inventory.entity.SyDictData;
import com.syyang.inventory.service.SyDictDataService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.SyDictDataPageParam;
import com.syyang.springbootplus.framework.common.controller.BaseController;
import com.syyang.springbootplus.framework.common.api.ApiResult;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.common.param.IdParam;
import com.syyang.springbootplus.framework.log.annotation.Module;
import com.syyang.springbootplus.framework.log.annotation.OperationLog;
import com.syyang.springbootplus.framework.log.enums.OperationLogType;
import com.syyang.springbootplus.framework.core.validator.groups.Add;
import com.syyang.springbootplus.framework.core.validator.groups.Update;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 字典数据表 控制器
 *
 * @author syyang
 * @since 2023-03-11
 */
@Slf4j
@RestController
@RequestMapping("/syDictData")
@Module("inventory")
@Api(value = "字典数据表API", tags = {"字典数据表"})
public class SyDictDataController extends BaseController {

    @Autowired
    private SyDictDataService syDictDataService;

    /**
     * 添加字典数据表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加字典数据表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加字典数据表", response = ApiResult.class)
    public ApiResult<Boolean> addSyDictData(@Validated(Add.class) @RequestBody SyDictData syDictData) throws Exception {
        boolean flag = syDictDataService.saveSyDictData(syDictData);
        return ApiResult.result(flag);
    }

    /**
     * 修改字典数据表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改字典数据表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改字典数据表", response = ApiResult.class)
    public ApiResult<Boolean> updateSyDictData(@Validated(Update.class) @RequestBody SyDictData syDictData) throws Exception {
        boolean flag = syDictDataService.updateSyDictData(syDictData);
        return ApiResult.result(flag);
    }

    /**
     * 删除字典数据表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除字典数据表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除字典数据表", response = ApiResult.class)
    public ApiResult<Boolean> deleteSyDictData(@PathVariable("id") Long id) throws Exception {
        boolean flag = syDictDataService.deleteSyDictData(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取字典数据表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "字典数据表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "字典数据表详情", response = SyDictData.class)
    public ApiResult<SyDictData> getSyDictData(@PathVariable("id") Long id) throws Exception {
        SyDictData syDictData = syDictDataService.getById(id);
        return ApiResult.ok(syDictData);
    }

    /**
     * 字典数据表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "字典数据表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "字典数据表分页列表", response = SyDictData.class)
    public ApiResult<Paging<SyDictData>> getSyDictDataPageList(@Validated @RequestBody SyDictDataPageParam syDictDataPageParam) throws Exception {
        Paging<SyDictData> paging = syDictDataService.getSyDictDataPageList(syDictDataPageParam);
        return ApiResult.ok(paging);
    }

/**
 * 字典数据表列表
 */
    @PostMapping("/getList")
        @OperationLog(name = "字典数据表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "字典数据表列表", response = SyDictData.class)
    public ApiResult<List<SyDictData>> getSyDictDataList(@Validated @RequestBody SyDictDataPageParam syDictDataPageParam) throws Exception {
        List<SyDictData> dataList = syDictDataService.getSyDictDataList(syDictDataPageParam);
        return ApiResult.ok(dataList);
    }

}

