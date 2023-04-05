package com.syyang.inventory.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.syyang.springbootplus.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 项目其他支出信息表 分页参数对象
 * </pre>
 *
 * @author syyang
 * @date 2023-04-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目其他支出信息表分页参数")
public class InventoryProjectOtherBusinessPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("交易状态 1表示提交审批 2表示通过 -1表示未通过 3表示已入账 4表示未入账")
    private String status;

    @ApiModelProperty("项目id")
    private Integer projectId;


    @ApiModelProperty("审核人id")
    private String approver;

    @ApiModelProperty("出纳人id")
    private String cashier;
}
