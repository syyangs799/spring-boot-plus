package com.syyang.inventory.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.syyang.springbootplus.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 产品类型码表 分页参数对象
 * </pre>
 *
 * @author syyang
 * @date 2023-04-01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "产品类型码表分页参数")
public class InventoryProductTypePageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
