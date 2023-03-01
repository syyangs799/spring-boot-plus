package com.syyang.inventory.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.syyang.springbootplus.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * FooBar 分页参数对象
 * </pre>
 *
 * @author syyang
 * @date 2023-02-26
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FooBar分页参数")
public class FooBarPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
