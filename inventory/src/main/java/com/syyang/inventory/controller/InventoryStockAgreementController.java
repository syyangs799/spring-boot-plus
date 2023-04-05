package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryStockAgreement;
import com.syyang.inventory.service.InventoryStockAgreementService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryStockAgreementPageParam;
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
 * 库存合同表 控制器
 *
 * @author syyang
 * @since 2023-04-05
 */
@Slf4j
@RestController
@RequestMapping("/inventoryStockAgreement")
@Module("inventory")
@Api(value = "库存合同表API", tags = {"库存合同表"})
public class InventoryStockAgreementController extends BaseController {

    @Autowired
    private InventoryStockAgreementService inventoryStockAgreementService;

    /**
     * 添加库存合同表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加库存合同表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加库存合同表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryStockAgreement(@Validated(Add.class) @RequestBody InventoryStockAgreement inventoryStockAgreement) throws Exception {
        boolean flag = inventoryStockAgreementService.saveInventoryStockAgreement(inventoryStockAgreement);
        return ApiResult.result(flag);
    }

    /**
     * 修改库存合同表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改库存合同表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改库存合同表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryStockAgreement(@Validated(Update.class) @RequestBody InventoryStockAgreement inventoryStockAgreement) throws Exception {
        boolean flag = inventoryStockAgreementService.updateInventoryStockAgreement(inventoryStockAgreement);
        return ApiResult.result(flag);
    }

    /**
     * 删除库存合同表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除库存合同表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除库存合同表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryStockAgreement(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryStockAgreementService.deleteInventoryStockAgreement(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取库存合同表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "库存合同表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "库存合同表详情", response = InventoryStockAgreement.class)
    public ApiResult<InventoryStockAgreement> getInventoryStockAgreement(@PathVariable("id") Long id) throws Exception {
        InventoryStockAgreement inventoryStockAgreement = inventoryStockAgreementService.getById(id);
        return ApiResult.ok(inventoryStockAgreement);
    }

    /**
     * 库存合同表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "库存合同表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "库存合同表分页列表", response = InventoryStockAgreement.class)
    public ApiResult<Paging<InventoryStockAgreement>> getInventoryStockAgreementPageList(@Validated @RequestBody InventoryStockAgreementPageParam inventoryStockAgreementPageParam) throws Exception {
        Paging<InventoryStockAgreement> paging = inventoryStockAgreementService.getInventoryStockAgreementPageList(inventoryStockAgreementPageParam);
        return ApiResult.ok(paging);
    }

/**
 * 库存合同表列表
 */
    @PostMapping("/getList")
        @OperationLog(name = "库存合同表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "库存合同表列表", response = InventoryStockAgreement.class)
    public ApiResult<List<InventoryStockAgreement>> getInventoryStockAgreementList(@Validated @RequestBody InventoryStockAgreementPageParam inventoryStockAgreementPageParam) throws Exception {
        List<InventoryStockAgreement> dataList = inventoryStockAgreementService.getInventoryStockAgreementList(inventoryStockAgreementPageParam);
        return ApiResult.ok(dataList);
    }

}

