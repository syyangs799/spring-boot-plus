package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProductType;
import com.syyang.inventory.service.InventoryProductTypeService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryProductTypePageParam;
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
 * 产品类型码表 控制器
 *
 * @author syyang
 * @since 2023-04-01
 */
@Slf4j
@RestController
@RequestMapping("/inventoryProductType")
@Module("inventory")
@Api(value = "产品类型码表API", tags = {"产品类型码表"})
public class InventoryProductTypeController extends BaseController {

    @Autowired
    private InventoryProductTypeService inventoryProductTypeService;

    /**
     * 添加产品类型码表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加产品类型码表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加产品类型码表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryProductType(@Validated(Add.class) @RequestBody InventoryProductType inventoryProductType) throws Exception {
        boolean flag = inventoryProductTypeService.saveInventoryProductType(inventoryProductType);
        return ApiResult.result(flag);
    }

    /**
     * 修改产品类型码表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改产品类型码表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改产品类型码表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryProductType(@Validated(Update.class) @RequestBody InventoryProductType inventoryProductType) throws Exception {
        boolean flag = inventoryProductTypeService.updateInventoryProductType(inventoryProductType);
        return ApiResult.result(flag);
    }

    /**
     * 删除产品类型码表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除产品类型码表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除产品类型码表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryProductType(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryProductTypeService.deleteInventoryProductType(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取产品类型码表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "产品类型码表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "产品类型码表详情", response = InventoryProductType.class)
    public ApiResult<InventoryProductType> getInventoryProductType(@PathVariable("id") Long id) throws Exception {
        InventoryProductType inventoryProductType = inventoryProductTypeService.getById(id);
        return ApiResult.ok(inventoryProductType);
    }

    /**
     * 产品类型码表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "产品类型码表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "产品类型码表分页列表", response = InventoryProductType.class)
    public ApiResult<Paging<InventoryProductType>> getInventoryProductTypePageList(@Validated @RequestBody InventoryProductTypePageParam inventoryProductTypePageParam) throws Exception {
        Paging<InventoryProductType> paging = inventoryProductTypeService.getInventoryProductTypePageList(inventoryProductTypePageParam);
        return ApiResult.ok(paging);
    }

/**
 * 产品类型码表列表
 */
    @PostMapping("/getList")
        @OperationLog(name = "产品类型码表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "产品类型码表列表", response = InventoryProductType.class)
    public ApiResult<List<InventoryProductType>> getInventoryProductTypeList(@Validated @RequestBody InventoryProductTypePageParam inventoryProductTypePageParam) throws Exception {
        List<InventoryProductType> dataList = inventoryProductTypeService.getInventoryProductTypeList(inventoryProductTypePageParam);
        return ApiResult.ok(dataList);
    }

}

