package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.service.InventoryProductInfoService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
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
 * 产品信息表 控制器
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@RestController
@RequestMapping("/inventoryProductInfo")
@Module("inventory")
@Api(value = "产品信息表API", tags = {"产品信息表"})
public class InventoryProductInfoController extends BaseController {

    @Autowired
    private InventoryProductInfoService inventoryProductInfoService;

    /**
     * 添加产品信息表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加产品信息表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加产品信息表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryProductInfo(@Validated(Add.class) @RequestBody InventoryProductInfo inventoryProductInfo) throws Exception {
        boolean flag = inventoryProductInfoService.saveInventoryProductInfo(inventoryProductInfo);
        return ApiResult.result(flag);
    }

    /**
     * 修改产品信息表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改产品信息表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改产品信息表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryProductInfo(@Validated(Update.class) @RequestBody InventoryProductInfo inventoryProductInfo) throws Exception {
        boolean flag = inventoryProductInfoService.updateInventoryProductInfo(inventoryProductInfo);
        return ApiResult.result(flag);
    }

    /**
     * 删除产品信息表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除产品信息表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除产品信息表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryProductInfo(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryProductInfoService.deleteInventoryProductInfo(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取产品信息表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "产品信息表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "产品信息表详情", response = InventoryProductInfo.class)
    public ApiResult<InventoryProductInfo> getInventoryProductInfo(@PathVariable("id") Long id) throws Exception {
        InventoryProductInfo inventoryProductInfo = inventoryProductInfoService.getById(id);
        return ApiResult.ok(inventoryProductInfo);
    }

    /**
     * 产品信息表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "产品信息表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "产品信息表分页列表", response = InventoryProductInfo.class)
    public ApiResult<Paging<InventoryProductInfo>> getInventoryProductInfoPageList(@Validated @RequestBody InventoryProductInfoPageParam inventoryProductInfoPageParam) throws Exception {
        Paging<InventoryProductInfo> paging = inventoryProductInfoService.getInventoryProductInfoPageList(inventoryProductInfoPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 产品信息表列表
     */
    @PostMapping("/getList")
    @OperationLog(name = "产品信息表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "产品信息表列表", response = InventoryProductInfo.class)
    public ApiResult<List<InventoryProductInfo>> getInventoryProductInfoList(@Validated @RequestBody InventoryProductInfoPageParam inventoryProductInfoPageParam) throws Exception {
        List<InventoryProductInfo> inventoryProductInfos = inventoryProductInfoService.getInventoryProductInfoList(inventoryProductInfoPageParam);
        return ApiResult.ok(inventoryProductInfos);
    }
}

