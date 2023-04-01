package com.syyang.inventory.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 饼图、数值等vo
 *
 * @author syyang
 * @since 2023-03-02
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "级联饼图、数值等vo")
public class KeyAndValue2Vo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("key")
    private String key;

    @ApiModelProperty("value")
    private String value;

    @ApiModelProperty("child")
    List<KeyAndValueVo> child;


    public KeyAndValue2Vo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyAndValue2Vo(String key, String value, List<KeyAndValueVo> child) {
        this.key = key;
        this.value = value;
        this.child = child;
    }
}
