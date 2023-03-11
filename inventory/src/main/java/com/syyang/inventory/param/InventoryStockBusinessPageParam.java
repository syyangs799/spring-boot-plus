package com.syyang.inventory.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.syyang.springbootplus.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 库存交易流水表 分页参数对象
 * </pre>
 *
 * @author syyang
 * @date 2023-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "库存交易流水表分页参数")
public class InventoryStockBusinessPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品码表")
    private String productId;


    @ApiModelProperty("交易状态 0表示创建 1表示提交审批 2表示通过 -1表示未通过 3表示已入账 4表示未入账")
    private String status;
}
