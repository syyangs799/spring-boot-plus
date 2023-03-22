package com.syyang.inventory.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
@Data
@Accessors(chain = true)
@ApiModel(value = "饼图、数值等vo")
public class EChartXAxisVo implements Serializable {


    private static final long serialVersionUID = -1;

    /**
     * 数据
     */
    private List<String> data;

    /**
     * 横轴名称
     */
    private String name;
}

