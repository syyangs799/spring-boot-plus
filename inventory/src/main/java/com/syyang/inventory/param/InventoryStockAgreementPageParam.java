package com.syyang.inventory.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.syyang.springbootplus.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 库存合同表 分页参数对象
 * </pre>
 *
 * @author syyang
 * @date 2023-04-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "库存合同表分页参数")
public class InventoryStockAgreementPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
