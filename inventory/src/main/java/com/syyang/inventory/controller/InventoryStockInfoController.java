package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryStockInfo;
import com.syyang.inventory.service.InventoryStockInfoService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryStockInfoPageParam;
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

/**
 * 库存信息表 控制器
 *
 * @author syyang
 * @since 2023-03-01
 */
@Slf4j
@RestController
@RequestMapping("/inventoryStockInfo")
@Module("inventory")
@Api(value = "库存信息表API", tags = {"库存信息表"})
public class InventoryStockInfoController extends BaseController {

    @Autowired
    private InventoryStockInfoService inventoryStockInfoService;

    /**
     * 添加库存信息表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加库存信息表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加库存信息表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryStockInfo(@Validated(Add.class) @RequestBody InventoryStockInfo inventoryStockInfo) throws Exception {
        boolean flag = inventoryStockInfoService.saveInventoryStockInfo(inventoryStockInfo);
        return ApiResult.result(flag);
    }

    /**
     * 修改库存信息表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改库存信息表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改库存信息表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryStockInfo(@Validated(Update.class) @RequestBody InventoryStockInfo inventoryStockInfo) throws Exception {
        boolean flag = inventoryStockInfoService.updateInventoryStockInfo(inventoryStockInfo);
        return ApiResult.result(flag);
    }

    /**
     * 删除库存信息表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除库存信息表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除库存信息表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryStockInfo(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryStockInfoService.deleteInventoryStockInfo(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取库存信息表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "库存信息表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "库存信息表详情", response = InventoryStockInfo.class)
    public ApiResult<InventoryStockInfo> getInventoryStockInfo(@PathVariable("id") Long id) throws Exception {
        InventoryStockInfo inventoryStockInfo = inventoryStockInfoService.getById(id);
        return ApiResult.ok(inventoryStockInfo);
    }

    /**
     * 库存信息表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "库存信息表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "库存信息表分页列表", response = InventoryStockInfo.class)
    public ApiResult<Paging<InventoryStockInfo>> getInventoryStockInfoPageList(@Validated @RequestBody InventoryStockInfoPageParam inventoryStockInfoPageParam) throws Exception {
        Paging<InventoryStockInfo> paging = inventoryStockInfoService.getInventoryStockInfoPageList(inventoryStockInfoPageParam);
        return ApiResult.ok(paging);
    }

}

