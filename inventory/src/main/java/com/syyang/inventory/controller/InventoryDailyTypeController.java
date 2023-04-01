package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryDailyType;
import com.syyang.inventory.service.InventoryDailyTypeService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryDailyTypePageParam;
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
 * 日常支出类型码表 控制器
 *
 * @author syyang
 * @since 2023-04-01
 */
@Slf4j
@RestController
@RequestMapping("/inventoryDailyType")
@Module("inventory")
@Api(value = "日常支出类型码表API", tags = {"日常支出类型码表"})
public class InventoryDailyTypeController extends BaseController {

    @Autowired
    private InventoryDailyTypeService inventoryDailyTypeService;

    /**
     * 添加日常支出类型码表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加日常支出类型码表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加日常支出类型码表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryDailyType(@Validated(Add.class) @RequestBody InventoryDailyType inventoryDailyType) throws Exception {
        boolean flag = inventoryDailyTypeService.saveInventoryDailyType(inventoryDailyType);
        return ApiResult.result(flag);
    }

    /**
     * 修改日常支出类型码表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改日常支出类型码表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改日常支出类型码表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryDailyType(@Validated(Update.class) @RequestBody InventoryDailyType inventoryDailyType) throws Exception {
        boolean flag = inventoryDailyTypeService.updateInventoryDailyType(inventoryDailyType);
        return ApiResult.result(flag);
    }

    /**
     * 删除日常支出类型码表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除日常支出类型码表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除日常支出类型码表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryDailyType(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryDailyTypeService.deleteInventoryDailyType(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取日常支出类型码表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "日常支出类型码表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "日常支出类型码表详情", response = InventoryDailyType.class)
    public ApiResult<InventoryDailyType> getInventoryDailyType(@PathVariable("id") Long id) throws Exception {
        InventoryDailyType inventoryDailyType = inventoryDailyTypeService.getById(id);
        return ApiResult.ok(inventoryDailyType);
    }

    /**
     * 日常支出类型码表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "日常支出类型码表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "日常支出类型码表分页列表", response = InventoryDailyType.class)
    public ApiResult<Paging<InventoryDailyType>> getInventoryDailyTypePageList(@Validated @RequestBody InventoryDailyTypePageParam inventoryDailyTypePageParam) throws Exception {
        Paging<InventoryDailyType> paging = inventoryDailyTypeService.getInventoryDailyTypePageList(inventoryDailyTypePageParam);
        return ApiResult.ok(paging);
    }

/**
 * 日常支出类型码表列表
 */
    @PostMapping("/getList")
        @OperationLog(name = "日常支出类型码表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "日常支出类型码表列表", response = InventoryDailyType.class)
    public ApiResult<List<InventoryDailyType>> getInventoryDailyTypeList(@Validated @RequestBody InventoryDailyTypePageParam inventoryDailyTypePageParam) throws Exception {
        List<InventoryDailyType> dataList = inventoryDailyTypeService.getInventoryDailyTypeList(inventoryDailyTypePageParam);
        return ApiResult.ok(dataList);
    }

}

