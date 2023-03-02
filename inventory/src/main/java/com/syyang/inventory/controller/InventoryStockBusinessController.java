package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryStockBusiness;
import com.syyang.inventory.service.InventoryStockBusinessService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryStockBusinessPageParam;
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
 * 库存交易流水表 控制器
 *
 * @author syyang
 * @since 2023-03-01
 */
@Slf4j
@RestController
@RequestMapping("/inventoryStockBusiness")
@Module("inventory")
@Api(value = "库存交易流水表API", tags = {"库存交易流水表"})
public class InventoryStockBusinessController extends BaseController {

    @Autowired
    private InventoryStockBusinessService inventoryStockBusinessService;

    /**
     * 添加库存交易流水表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加库存交易流水表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加库存交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryStockBusiness(@Validated(Add.class) @RequestBody InventoryStockBusiness inventoryStockBusiness) throws Exception {
        boolean flag = inventoryStockBusinessService.saveInventoryStockBusiness(inventoryStockBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 修改库存交易流水表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改库存交易流水表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改库存交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryStockBusiness(@Validated(Update.class) @RequestBody InventoryStockBusiness inventoryStockBusiness) throws Exception {
        boolean flag = inventoryStockBusinessService.updateInventoryStockBusiness(inventoryStockBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 删除库存交易流水表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除库存交易流水表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除库存交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryStockBusiness(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryStockBusinessService.deleteInventoryStockBusiness(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取库存交易流水表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "库存交易流水表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "库存交易流水表详情", response = InventoryStockBusiness.class)
    public ApiResult<InventoryStockBusiness> getInventoryStockBusiness(@PathVariable("id") Long id) throws Exception {
        InventoryStockBusiness inventoryStockBusiness = inventoryStockBusinessService.getById(id);
        return ApiResult.ok(inventoryStockBusiness);
    }

    /**
     * 库存交易流水表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "库存交易流水表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "库存交易流水表分页列表", response = InventoryStockBusiness.class)
    public ApiResult<Paging<InventoryStockBusiness>> getInventoryStockBusinessPageList(@Validated @RequestBody InventoryStockBusinessPageParam inventoryStockBusinessPageParam) throws Exception {
        Paging<InventoryStockBusiness> paging = inventoryStockBusinessService.getInventoryStockBusinessPageList(inventoryStockBusinessPageParam);
        return ApiResult.ok(paging);
    }

}

