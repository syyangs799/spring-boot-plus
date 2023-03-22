package com.syyang.inventory.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "饼图、数值等vo")
public class EChartVo implements Serializable {


    private static final long serialVersionUID = -1;

    /**
     * 横轴
     */
    private EChartXAxisVo xAxis;

    /**
     * 数据
     */
    private List<EChartSeriesVo> series;

}