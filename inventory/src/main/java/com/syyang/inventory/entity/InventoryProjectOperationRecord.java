package com.syyang.inventory.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.syyang.springbootplus.framework.common.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.syyang.springbootplus.framework.core.validator.groups.Update;

/**
 * 项目修改记录表
 *
 * @author syyang
 * @since 2023-03-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InventoryProjectOperationRecord对象")
public class InventoryProjectOperationRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("项目id")
    private Integer projectId;

    @ApiModelProperty("操作名称，自动生成，人+时间 + 操作类型")
    private String operationName;

    @ApiModelProperty("操作类型code 创建 和 更新 审核")
    private String operationType;
    @ApiModelProperty("操作类型名称 创建 和 更新 审核")
    private String operationTypeName;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    @ApiModelProperty("创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String createUserName;

    @ApiModelProperty("项目创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT)
    private String updateUser;
    @ApiModelProperty("修改人名称")
    @TableField(fill = FieldFill.INSERT)
    private String updateUserName;

    @ApiModelProperty("修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("更新内容")
    private String updateContent;

    @ApiModelProperty("部门id")
    @TableField(fill = FieldFill.INSERT)
    private Integer departmentId;

}
