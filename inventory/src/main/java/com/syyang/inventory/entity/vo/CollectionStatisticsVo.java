package com.syyang.inventory.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 饼图、数值等vo
 *
 * @author syyang
 * @since 2023-03-02
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "饼图、数值等vo")
public class CollectionStatisticsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("应收")
    private String receivables;

    @ApiModelProperty("已收")
    private String received;
    @ApiModelProperty("未收款")
    private String unReceived;

    public CollectionStatisticsVo(String projectName, String receivables, String received, String unReceived) {
        this.projectName = projectName;
        this.receivables = receivables;
        this.received = received;
        this.unReceived = unReceived;
    }
}
