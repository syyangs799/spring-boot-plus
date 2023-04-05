package com.syyang.inventory.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.syyang.springbootplus.framework.common.annotationun.LogForUpdate;
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
 * 库存合同表
 *
 * @author syyang
 * @since 2023-04-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InventoryStockAgreement对象")
public class InventoryStockAgreement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("合同编号id")
    private String agreementId;

    @ApiModelProperty("合同名称")
    private String agreementName;

    @ApiModelProperty("交易类型，0 采购合同 1出库合同")
    private Integer type;

    @ApiModelProperty("合同金额-小计")
    private String agreementAmount;

    @ApiModelProperty("合同厂家")
    private String agreementManufactor;

    @ApiModelProperty("合同时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime agreementTime;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("创建人名称")
    private String createUserName;

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

    @ApiModelProperty("交易状态 1表示提交审批 2表示通过 -1表示未通过 3表示已入账 4表示未入账")
    private String status;

    @ApiModelProperty("部门id，用作区分不同的项目id")
    private Integer departmentId;

    @ApiModelProperty("审核人")
    private String approver;

    @ApiModelProperty("审核人名称")
    private String approverName;

    @ApiModelProperty("审核时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime approverTime;

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

    @ApiModelProperty("附件名称")
    private String fileName;

    @ApiModelProperty("附件path")
    private String filePath;

}
