package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProjectOperationRecord;
import com.syyang.inventory.service.InventoryProjectOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryProjectOperationRecordPageParam;
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
 * 项目修改记录表 控制器
 *
 * @author syyang
 * @since 2023-03-12
 */
@Slf4j
@RestController
@RequestMapping("/inventoryProjectOperationRecord")
@Module("inventory")
@Api(value = "项目修改记录表API", tags = {"项目修改记录表"})
public class InventoryProjectOperationRecordController extends BaseController {

    @Autowired
    private InventoryProjectOperationRecordService inventoryProjectOperationRecordService;

    /**
     * 添加项目修改记录表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加项目修改记录表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加项目修改记录表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryProjectOperationRecord(@Validated(Add.class) @RequestBody InventoryProjectOperationRecord inventoryProjectOperationRecord) throws Exception {
        boolean flag = inventoryProjectOperationRecordService.saveInventoryProjectOperationRecord(inventoryProjectOperationRecord);
        return ApiResult.result(flag);
    }

    /**
     * 修改项目修改记录表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改项目修改记录表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改项目修改记录表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryProjectOperationRecord(@Validated(Update.class) @RequestBody InventoryProjectOperationRecord inventoryProjectOperationRecord) throws Exception {
        boolean flag = inventoryProjectOperationRecordService.updateInventoryProjectOperationRecord(inventoryProjectOperationRecord);
        return ApiResult.result(flag);
    }

    /**
     * 删除项目修改记录表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除项目修改记录表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除项目修改记录表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryProjectOperationRecord(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryProjectOperationRecordService.deleteInventoryProjectOperationRecord(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取项目修改记录表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "项目修改记录表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "项目修改记录表详情", response = InventoryProjectOperationRecord.class)
    public ApiResult<InventoryProjectOperationRecord> getInventoryProjectOperationRecord(@PathVariable("id") Long id) throws Exception {
        InventoryProjectOperationRecord inventoryProjectOperationRecord = inventoryProjectOperationRecordService.getById(id);
        return ApiResult.ok(inventoryProjectOperationRecord);
    }

    /**
     * 项目修改记录表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "项目修改记录表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "项目修改记录表分页列表", response = InventoryProjectOperationRecord.class)
    public ApiResult<Paging<InventoryProjectOperationRecord>> getInventoryProjectOperationRecordPageList(@Validated @RequestBody InventoryProjectOperationRecordPageParam inventoryProjectOperationRecordPageParam) throws Exception {
        Paging<InventoryProjectOperationRecord> paging = inventoryProjectOperationRecordService.getInventoryProjectOperationRecordPageList(inventoryProjectOperationRecordPageParam);
        return ApiResult.ok(paging);
    }

/**
 * 项目修改记录表列表
 */
    @PostMapping("/getList")
        @OperationLog(name = "项目修改记录表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "项目修改记录表列表", response = InventoryProjectOperationRecord.class)
    public ApiResult<List<InventoryProjectOperationRecord>> getInventoryProjectOperationRecordList(@Validated @RequestBody InventoryProjectOperationRecordPageParam inventoryProjectOperationRecordPageParam) throws Exception {
        List<InventoryProjectOperationRecord> dataList = inventoryProjectOperationRecordService.getInventoryProjectOperationRecordList(inventoryProjectOperationRecordPageParam);
        return ApiResult.ok(dataList);
    }

}

