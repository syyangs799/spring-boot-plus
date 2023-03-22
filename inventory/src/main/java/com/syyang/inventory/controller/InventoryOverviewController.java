package com.syyang.inventory.controller;

import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.vo.EChartVo;
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
    @OperationLog(name = "大屏-中上-项目收支相关统计-标签卡", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-中上-项目收支相关统计-标签卡", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getProjectFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        List<KeyAndValueVo> keyAndValueVos = inventoryOverviewService.getProjectFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }

    /**
     * 项目收支相关统计
     * 7.项目统计---横轴时间-纵轴数量
     * 新建，实施，完成-时间
     */
    @GetMapping("/projectStatusFinance")
    @OperationLog(name = "大屏-右中-项目状态相关统计-时间-项目梳理折线图", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-右中-项目状态相关统计-时间-项目梳理折线图", response = InventoryProjectInfo.class)
    public ApiResult<EChartVo> getProjectStatusFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        EChartVo keyAndValueVos = inventoryOverviewService.getProjectStatusFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }

    /**
     * 日常收支相关统计
     */
    @GetMapping("/dailyFinance")
    @OperationLog(name = "大屏-左下-日常收支相关统计-柱状图", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-左下-日常收支相关统计-柱状图", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getDailyFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        List<KeyAndValueVo> keyAndValueVos = inventoryOverviewService.getDailyFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }

    /**
     * 经营情况相关统计
     * 应收款和未收款-----时间
     */
    @GetMapping("/manageFinance")
    @OperationLog(name = "大屏-中下-经营情况相关统计（折线图还是饼图）", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-中下-经营情况相关统计（折线图还是饼图）", response = InventoryProjectInfo.class)
    public ApiResult<List<KeyAndValueVo>> getManageFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        List<KeyAndValueVo> keyAndValueVos = inventoryOverviewService.getManageFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }


    /**
     * 收入支出相关统计
     */
    @GetMapping("/expensesAndEeceiptsFinance")
    @OperationLog(name = "大屏-左上-收支-出纳相关统计-时间-折线图", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-左上-收支-出纳相关统计-时间-折线图", response = InventoryProjectInfo.class)
    public ApiResult<Map<String,List<KeyAndValueVo>>> getExpensesAndEeceiptsFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        Map<String,List<KeyAndValueVo>> keyAndValueVos = inventoryOverviewService.getExpensesAndEeceiptsFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }
    /**
     * 10.应收款=各项支出+利润（）
     */
    @GetMapping("/receivablesFinance")
    @OperationLog(name = "大屏-右上-因收款-相关统计-时间-折线图", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-右上-因收款-相关统计-时间-折线图", response = InventoryProjectInfo.class)
    public ApiResult<Map<String,List<KeyAndValueVo>>> getReceivablesFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        Map<String,List<KeyAndValueVo>> keyAndValueVos = inventoryOverviewService.getExpensesAndEeceiptsFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }


    /**
     * 10.应收款=各项支出+利润（）
     */
    @GetMapping("/profitFinance")
    @OperationLog(name = "大屏-左中-利润-相关统计-时间-折线图", type = OperationLogType.INFO)
    @ApiOperation(value = "大屏-左中-利润-相关统计-时间-折线图", response = InventoryProjectInfo.class)
    public ApiResult<Map<String,List<KeyAndValueVo>>> getProfitFinance(@Validated @RequestBody InventoryOverviewParam inventoryOverviewParam) throws Exception {
        Map<String,List<KeyAndValueVo>> keyAndValueVos = inventoryOverviewService.getExpensesAndEeceiptsFinance(inventoryOverviewParam);
        return ApiResult.ok(keyAndValueVos);
    }



}

