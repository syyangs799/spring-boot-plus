package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.param.InventoryOverviewParam;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
import com.syyang.inventory.param.InventoryProjectInfoPageParam;
import com.syyang.inventory.service.InventoryOverviewService;
import com.syyang.inventory.service.InventoryProjectInfoService;
import com.syyang.springbootplus.framework.common.api.ApiResult;
import com.syyang.springbootplus.framework.common.controller.BaseController;
import com.syyang.springbootplus.framework.core.pagination.Paging;
import com.syyang.springbootplus.framework.core.validator.groups.Add;
import com.syyang.springbootplus.framework.core.validator.groups.Update;
import com.syyang.springbootplus.framework.log.annotation.Module;
import com.syyang.springbootplus.framework.log.annotation.OperationLog;
import com.syyang.springbootplus.framework.log.enums.OperationLogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 大屏总览效果 控制器
 *
 * @author syyang
 * @since 2023-03-02
 */
@Slf4j
@RestController
@RequestMapping("/inventoryOverview")
@Module("inventory")
@Api(value = "大屏API", tags = {"大屏API"})
public class InventoryOverviewController extends BaseController {

    @Autowired
    private InventoryOverviewService inventoryOverviewService;

    /**
     * 项目收支相关统计
     */
    @GetMapping("/projectFinance")
    @OperationLog(name = "大屏-项目收支相关统计", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-项目收支相关统计", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getProjectFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        List<KeyAndValueVo> keyAndValueVos = inventoryOverviewService.getProjectFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }

    /**
     * 项目收支相关统计
     */
    @GetMapping("/projectStatusFinance")
    @OperationLog(name = "大屏-项目状态相关统计-新建，实施，完成", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-项目状态相关统计-新建，实施，完成", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getProjectStatusFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        List<KeyAndValueVo> keyAndValueVos = inventoryOverviewService.getProjectStatusFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }

    /**
     * 日常收支相关统计
     */
    @GetMapping("/dailyFinance")
    @OperationLog(name = "大屏-日常收支相关统计", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-日常收支相关统计", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getDailyFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        List<KeyAndValueVo> keyAndValueVos = inventoryOverviewService.getDailyFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }

    /**
     * 经营情况相关统计
     */
    @GetMapping("/dailyFinance")
    @OperationLog(name = "大屏-经营情况相关统计", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-经营情况相关统计", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getManageFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        List<KeyAndValueVo> keyAndValueVos = inventoryOverviewService.getManageFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }


    /**
     * 收入支出相关统计
     */
    @GetMapping("/expensesAndEeceiptsFinance")
    @OperationLog(name = "大屏-经营情况相关统计", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-经营情况相关统计", response = InventoryProjectInfo.class)
    public ApiResult<Map<String,List<KeyAndValueVo>>> getExpensesAndEeceiptsFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        Map<String,List<KeyAndValueVo>> keyAndValueVos = inventoryOverviewService.getExpensesAndEeceiptsFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }
}

