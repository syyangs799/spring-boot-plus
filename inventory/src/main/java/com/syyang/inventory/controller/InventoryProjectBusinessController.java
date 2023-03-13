package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProjectBusiness;
import com.syyang.inventory.service.InventoryProjectBusinessService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryProjectBusinessPageParam;
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
 * 项目收入与支出交易流水表 控制器
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@RestController
@RequestMapping("/inventoryProjectBusiness")
@Module("inventory")
@Api(value = "项目收入与支出交易流水表API", tags = {"项目收入与支出交易流水表"})
public class InventoryProjectBusinessController extends BaseController {

    @Autowired
    private InventoryProjectBusinessService inventoryProjectBusinessService;

    /**
     * 添加项目收入与支出交易流水表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加项目收入与支出交易流水表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加项目收入与支出交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryProjectBusiness(@Validated(Add.class) @RequestBody InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        boolean flag = inventoryProjectBusinessService.saveInventoryProjectBusiness(inventoryProjectBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 修改项目收入与支出交易流水表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改项目收入与支出交易流水表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改项目收入与支出交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryProjectBusiness(@Validated(Update.class) @RequestBody InventoryProjectBusiness inventoryProjectBusiness) throws Exception {
        boolean flag = inventoryProjectBusinessService.updateInventoryProjectBusiness(inventoryProjectBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 删除项目收入与支出交易流水表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除项目收入与支出交易流水表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除项目收入与支出交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryProjectBusiness(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryProjectBusinessService.deleteInventoryProjectBusiness(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取项目收入与支出交易流水表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "项目收入与支出交易流水表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "项目收入与支出交易流水表详情", response = InventoryProjectBusiness.class)
    public ApiResult<InventoryProjectBusiness> getInventoryProjectBusiness(@PathVariable("id") Long id) throws Exception {
        InventoryProjectBusiness inventoryProjectBusiness = inventoryProjectBusinessService.getById(id);
        return ApiResult.ok(inventoryProjectBusiness);
    }

    /**
     * 项目收入与支出交易流水表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "项目收入与支出交易流水表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "项目收入与支出交易流水表分页列表", response = InventoryProjectBusiness.class)
    public ApiResult<Paging<InventoryProjectBusiness>> getInventoryProjectBusinessPageList(@Validated @RequestBody InventoryProjectBusinessPageParam inventoryProjectBusinessPageParam) throws Exception {
        Paging<InventoryProjectBusiness> paging = inventoryProjectBusinessService.getInventoryProjectBusinessPageList(inventoryProjectBusinessPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 项目收入与支出交易流水表分页列表
     */
    @PostMapping("/getList")
    @OperationLog(name = "项目收入与支出交易流水表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "项目收入与支出交易流水表列表", response = InventoryProjectBusiness.class)
    public ApiResult<List<InventoryProjectBusiness>> getInventoryProjectBusinessList(@Validated @RequestBody InventoryProjectBusinessPageParam inventoryProjectBusinessPageParam) throws Exception {
        List<InventoryProjectBusiness> paging = inventoryProjectBusinessService.getInventoryProjectBusinessList(inventoryProjectBusinessPageParam);
        return ApiResult.ok(paging);
    }

}

