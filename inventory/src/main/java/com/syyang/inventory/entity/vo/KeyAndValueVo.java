package com.syyang.inventory.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.syyang.springbootplus.framework.common.entity.BaseEntity;
import com.syyang.springbootplus.framework.core.validator.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 饼图、数值等vo
 *
 * @author syyang
 * @since 2023-03-02
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "饼图、数值等vo")
public class KeyAndValueVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("key")
    private String key;

    @ApiModelProperty("value")
    private String value;

    public KeyAndValueVo(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
