package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryDailyBusiness;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.service.InventoryDailyBusinessService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryDailyBusinessPageParam;
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
 * 日常收入与支出交易流水表 控制器
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@RestController
@RequestMapping("/inventoryDailyBusiness")
@Module("inventory")
@Api(value = "日常收入与支出交易流水表API", tags = {"日常收入与支出交易流水表"})
public class InventoryDailyBusinessController extends BaseController {

    @Autowired
    private InventoryDailyBusinessService inventoryDailyBusinessService;

    /**
     * 添加日常收入与支出交易流水表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加日常收入与支出交易流水表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加日常收入与支出交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryDailyBusiness(@Validated(Add.class) @RequestBody InventoryDailyBusiness inventoryDailyBusiness) throws Exception {
        boolean flag = inventoryDailyBusinessService.saveInventoryDailyBusiness(inventoryDailyBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 修改日常收入与支出交易流水表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改日常收入与支出交易流水表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改日常收入与支出交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryDailyBusiness(@Validated(Update.class) @RequestBody InventoryDailyBusiness inventoryDailyBusiness) throws Exception {
        boolean flag = inventoryDailyBusinessService.updateInventoryDailyBusiness(inventoryDailyBusiness);
        return ApiResult.result(flag);
    }

    /**
     * 删除日常收入与支出交易流水表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除日常收入与支出交易流水表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除日常收入与支出交易流水表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryDailyBusiness(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryDailyBusinessService.deleteInventoryDailyBusiness(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取日常收入与支出交易流水表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "日常收入与支出交易流水表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "日常收入与支出交易流水表详情", response = InventoryDailyBusiness.class)
    public ApiResult<InventoryDailyBusiness> getInventoryDailyBusiness(@PathVariable("id") Long id) throws Exception {
        InventoryDailyBusiness inventoryDailyBusiness = inventoryDailyBusinessService.getById(id);
        return ApiResult.ok(inventoryDailyBusiness);
    }

    /**
     * 日常收入与支出交易流水表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "日常收入与支出交易流水表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "日常收入与支出交易流水表分页列表", response = InventoryDailyBusiness.class)
    public ApiResult<Paging<InventoryDailyBusiness>> getInventoryDailyBusinessPageList(@Validated @RequestBody InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) throws Exception {
        Paging<InventoryDailyBusiness> paging = inventoryDailyBusinessService.getInventoryDailyBusinessPageList(inventoryDailyBusinessPageParam);
        return ApiResult.ok(paging);
    }


    /**
     * 日常收入与支出交易流水表分页列表
     */
    @PostMapping("/getList")
    @OperationLog(name = "日常收入与支出交易流水表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "日常收入与支出交易流水表列表", response = InventoryDailyBusiness.class)
    public ApiResult<List<InventoryDailyBusiness>> getInventoryDailyBusinessList(@Validated @RequestBody InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) throws Exception {
        List<InventoryDailyBusiness> paging = inventoryDailyBusinessService.getInventoryDailyBusinessList(inventoryDailyBusinessPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 日常收入与支出总金额统计信息
     */
    @PostMapping("/getDailyBusinessAmount")
    @OperationLog(name = "日常收入与支出总金额统计信息", type = OperationLogType.LIST)
    @ApiOperation(value = "日常收入与支出总金额统计信息", response = InventoryDailyBusiness.class)
    public ApiResult<List<KeyAndValueVo>> getDailyBusinessAmount(@Validated @RequestBody InventoryDailyBusinessPageParam inventoryDailyBusinessPageParam) throws Exception {
        List<KeyAndValueVo> paging = inventoryDailyBusinessService.getDailyBusinessAmount(inventoryDailyBusinessPageParam);
        return ApiResult.ok(paging);
    }

}

