package com.syyang.inventory.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.syyang.springbootplus.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 产品信息表 分页参数对象
 * </pre>
 *
 * @author syyang
 * @date 2023-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "产品信息表分页参数")
public class InventoryProductInfoPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("产品类别")
    private String productType;
}
