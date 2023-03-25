package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.enums.StatusTypeEnum;
import com.syyang.inventory.service.InventoryProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import com.syyang.inventory.param.InventoryProjectInfoPageParam;
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
 * 项目信息表 控制器
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@RestController
@RequestMapping("/inventoryProjectInfo")
@Module("inventory")
@Api(value = "项目信息表API", tags = {"项目信息表"})
public class InventoryProjectInfoController extends BaseController {

    @Autowired
    private InventoryProjectInfoService inventoryProjectInfoService;

    /**
     * 添加项目信息表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加项目信息表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加项目信息表", response = ApiResult.class)
    public ApiResult<Boolean> addInventoryProjectInfo(@Validated(Add.class) @RequestBody InventoryProjectInfo inventoryProjectInfo) throws Exception {
        boolean flag = inventoryProjectInfoService.saveInventoryProjectInfo(inventoryProjectInfo);
        inventoryProjectInfoService.calculateProjectInformation(inventoryProjectInfo.getId());
        return ApiResult.result(flag);
    }

    /**
     * 修改项目信息表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改项目信息表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改项目信息表", response = ApiResult.class)
    public ApiResult<Boolean> updateInventoryProjectInfo(@Validated(Update.class) @RequestBody InventoryProjectInfo inventoryProjectInfo) throws Exception {
        boolean flag = inventoryProjectInfoService.updateInventoryProjectInfo(inventoryProjectInfo);
        inventoryProjectInfoService.calculateProjectInformation(inventoryProjectInfo.getId());
        return ApiResult.result(flag);
    }

    /**
     * 删除项目信息表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除项目信息表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除项目信息表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInventoryProjectInfo(@PathVariable("id") Long id) throws Exception {
        boolean flag = inventoryProjectInfoService.deleteInventoryProjectInfo(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取项目信息表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "项目信息表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "项目信息表详情", response = InventoryProjectInfo.class)
    public ApiResult<InventoryProjectInfo> getInventoryProjectInfo(@PathVariable("id") Long id) throws Exception {
        InventoryProjectInfo inventoryProjectInfo = inventoryProjectInfoService.getById(id);
        return ApiResult.ok(inventoryProjectInfo);
    }

    /**
     * 项目信息表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "项目信息表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "项目信息表分页列表", response = InventoryProjectInfo.class)
    public ApiResult<Paging<InventoryProjectInfo>> getInventoryProjectInfoPageList(@Validated @RequestBody InventoryProjectInfoPageParam inventoryProjectInfoPageParam) throws Exception {
        Paging<InventoryProjectInfo> paging = inventoryProjectInfoService.getInventoryProjectInfoPageList(inventoryProjectInfoPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 项目信息表分页列表
     */
    @PostMapping("/getList")
    @OperationLog(name = "项目信息表列表", type = OperationLogType.LIST)
    @ApiOperation(value = "项目信息表列表", response = InventoryProjectInfo.class)
    public ApiResult<List<InventoryProjectInfo>> getInventoryProjectInfoList(@Validated @RequestBody InventoryProjectInfoPageParam inventoryProjectInfoPageParam) throws Exception {
        List<InventoryProjectInfo> inventoryProjectInfos = inventoryProjectInfoService.getInventoryProjectInfoList(inventoryProjectInfoPageParam);
        return ApiResult.ok(inventoryProjectInfos);
    }

    /**
     * 项目金额统计信息列表
     */
    @PostMapping("/getTotalProjectAmount")
    @OperationLog(name = "项目金额统计信息列表", type = OperationLogType.LIST)
    @ApiOperation(value = "项目金额统计信息列表", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getTotalProjectAmount(@Validated @RequestBody InventoryProjectInfoPageParam inventoryProjectInfoPageParam) throws Exception {
        List<KeyAndValueVo> list = inventoryProjectInfoService.getTotalProjectAmount(inventoryProjectInfoPageParam);
        return ApiResult.ok(list);
    }

}

