package com.syyang.inventory.service;

import com.syyang.inventory.entity.InventoryProductInfo;
import com.syyang.inventory.entity.InventoryProjectInfo;
import com.syyang.inventory.entity.vo.EChartVo;
import com.syyang.inventory.entity.vo.KeyAndValueVo;
import com.syyang.inventory.param.InventoryOverviewParam;
import com.syyang.inventory.param.InventoryProductInfoPageParam;
import com.syyang.springbootplus.framework.common.service.BaseService;
import com.syyang.springbootplus.framework.core.pagination.Paging;

import java.util.List;
import java.util.Map;

/**
 * 产品信息表 服务类
 *
 * @author syyang
 * @since 2023-03-02
 */
public interface InventoryOverviewService extends BaseService<InventoryProductInfo> {


    List<KeyAndValueVo> getProjectFinance(InventoryOverviewParam inventoryOverviewParam);

    List<KeyAndValueVo> getDailyFinance(InventoryOverviewParam inventoryOverviewParam);

    List<KeyAndValueVo> getManageFinance(InventoryOverviewParam inventoryOverviewParam);

    List<KeyAndValueVo> getProjectStatusFinance(InventoryOverviewParam inventoryOverviewParam);

    EChartVo getExpensesAndEeceiptsFinance(InventoryOverviewParam inventoryOverviewParam);
}
