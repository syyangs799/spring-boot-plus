package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProjectOtherBusiness;
import com.syyang.inventory.service.InventoryProjectOtherBusinessService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryProjectOtherBusinessPageParam;
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
 * 项目其他支出信息表 控制器
 *
 * @author syyang
 * @since 2023-04-05
 */
@Slf4j
@RestController
@RequestMapping("/inventoryProjectOtherBusiness")
@Module("inventory")
@Api(value = "项目其他支出信息表API", tags = {"项目其他支出信息表"})
public class InventoryProjectOtherBusinessController extends BaseController {

    @Autowired
    private InventoryProjectOtherBusinessService inventoryProjectOtherBusinessService;

    /**
     * 添加项目其他支出信息表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加项目其他支出信息表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加项目其他支出信息表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryProjectOtherBusiness(@Validated(Add.class) @RequestBody InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception {
        boolean flag = inventoryProjectOtherBusinessService.saveInventoryProjectOtherBusiness(inventoryProjectOtherBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 修改项目其他支出信息表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改项目其他支出信息表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改项目其他支出信息表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryProjectOtherBusiness(@Validated(Update.class) @RequestBody InventoryProjectOtherBusiness inventoryProjectOtherBusiness) throws Exception {
        boolean flag = inventoryProjectOtherBusinessService.updateInventoryProjectOtherBusiness(inventoryProjectOtherBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 删除项目其他支出信息表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除项目其他支出信息表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除项目其他支出信息表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryProjectOtherBusiness(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryProjectOtherBusinessService.deleteInventoryProjectOtherBusiness(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取项目其他支出信息表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "项目其他支出信息表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "项目其他支出信息表详情", response = InventoryProjectOtherBusiness.class)
    public ApiResult<InventoryProjectOtherBusiness> getInventoryProjectOtherBusiness(@PathVariable("id") Long id) throws Exception {
        InventoryProjectOtherBusiness inventoryProjectOtherBusiness = inventoryProjectOtherBusinessService.getById(id);
        return ApiResult.ok(inventoryProjectOtherBusiness);
    }

    /**
     * 项目其他支出信息表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "项目其他支出信息表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "项目其他支出信息表分页列表", response = InventoryProjectOtherBusiness.class)
    public ApiResult<Paging<InventoryProjectOtherBusiness>> getInventoryProjectOtherBusinessPageList(@Validated @RequestBody InventoryProjectOtherBusinessPageParam inventoryProjectOtherBusinessPageParam) throws Exception {
        Paging<InventoryProjectOtherBusiness> paging = inventoryProjectOtherBusinessService.getInventoryProjectOtherBusinessPageList(inventoryProjectOtherBusinessPageParam);
        return ApiResult.ok(paging);
    }

/**
 * 项目其他支出信息表列表
 */
    @PostMapping("/getList")
        @OperationLog(name = "项目其他支出信息表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "项目其他支出信息表列表", response = InventoryProjectOtherBusiness.class)
    public ApiResult<List<InventoryProjectOtherBusiness>> getInventoryProjectOtherBusinessList(@Validated @RequestBody InventoryProjectOtherBusinessPageParam inventoryProjectOtherBusinessPageParam) throws Exception {
        List<InventoryProjectOtherBusiness> dataList = inventoryProjectOtherBusinessService.getInventoryProjectOtherBusinessList(inventoryProjectOtherBusinessPageParam);
        return ApiResult.ok(dataList);
    }

}

