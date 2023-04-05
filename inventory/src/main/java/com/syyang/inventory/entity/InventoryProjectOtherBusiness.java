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
 * 项目其他支出信息表
 *
 * @author syyang
 * @since 2023-04-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InventoryProjectOtherBusiness对象")
public class InventoryProjectOtherBusiness extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("项目其他支出自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("项目id")
    private Integer projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("交易类型 1支出")
    private Integer type;

    @ApiModelProperty("子类型")
    private String subType;

    @ApiModelProperty("项目收支子类型名称")
    private String subTypeName;

    @ApiModelProperty("金额")
    private String amountMoney;

    @ApiModelProperty("交易时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime businessTime;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("审核人名称")
    private String createUserName;

    @ApiModelProperty("交易状态 2表示通过 3表示已入账 4表示未入账")
    private String status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("附件名称")
    private String uploadFileName;

    @ApiModelProperty("附件路径")
    private String uploadFilePath;

    @ApiModelProperty("部门id，用作区分不同的部门权限")
    private Integer departmentId;

    @ApiModelProperty("出纳人")
    private String cashier;

    @ApiModelProperty("出纳人名称")
    private String cashierName;

    @ApiModelProperty("出纳时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime cashierTime;

    @ApiModelProperty("出纳核对金额")
    private String cashierAmount;

    @ApiModelProperty("出纳附件")
    private String cashierFileName;

    @ApiModelProperty("出纳附件路径")
    private String cashierFilePath;

    @ApiModelProperty("出纳意见")
    private String cashierIdea;

}
