package com.syyang.inventory.entity.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author: yyangs
 * @CreateDate: 2023/3/22 23:07
 * @UpdateDate: 2023/3/22 23:07
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "饼图、数值等vo")
public class EChartSeriesVo implements Serializable {

    private static final long serialVersionUID = -1;

    /**
     * 数据
     */
    private List<String> data;

    /**
     * 纵轴名称
     */
    private String name;

}
