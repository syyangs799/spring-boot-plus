package com.syyang.inventory.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.syyang.springbootplus.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 字典数据表 分页参数对象
 * </pre>
 *
 * @author syyang
 * @date 2023-03-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "字典数据表分页参数")
public class SyDictDataPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("字典类型 目前有project_step[项目进度] business_status[库存交易状态] daily_sub_type[日常流水子类型] project_sub_type[项目开支子类型]")
    private String dictType;


}
