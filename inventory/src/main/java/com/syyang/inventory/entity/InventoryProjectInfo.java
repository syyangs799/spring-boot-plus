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
 * 项目信息表
 *
 * @author syyang
 * @since 2023-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InventoryProjectInfo对象")
public class InventoryProjectInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("项目id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("商务负责人id")
    private Integer businesserId;

    @ApiModelProperty("商务负责人名称")
    private String businesserName;

    @ApiModelProperty("项目描述")
    private String projectDesc;

    @ApiModelProperty("技术负责人id")
    private Integer technicalerId;

    @ApiModelProperty("技术负责人名称")
    private String technicalerName;

    @ApiModelProperty("合同金额")
    private String amountContract;

    @ApiModelProperty("质保金")
    private String amountWarranty;

    @ApiModelProperty("质保金截止日期")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime amountWarrantyEnding;

    @ApiModelProperty("利润")
    private String amountProfit;

    @ApiModelProperty("商务费用")
    private String amountBusiness;

    @ApiModelProperty("商务费用已付 0表示未付 1表示已付")
    private Integer amountBusinessPaid;

    @ApiModelProperty("含税成本")
    private String amountCostTax;

    @ApiModelProperty("不含税成本")
    private String amountCostNoTax;

    @ApiModelProperty("税务成本")
    private String amountTax;

    @ApiModelProperty("税务成本已付 0表示未付 1表示已付")
    private Integer amountTaxPaid;

    @ApiModelProperty("业务提成")
    private String amountCommissionBusiness;

    @ApiModelProperty("业务提成已付 0表示未付 1表示已付")
    private Integer amountCommissionBusinessPaid;

    @ApiModelProperty("技术提成")
    private String amountCommissionTechnical;

    @ApiModelProperty("技术提成已付 0表示未付 1表示已付")
    private String amountCommissionTechnicalPaid;

    @ApiModelProperty("管理提成")
    private String amountCommissionManagement;

    @ApiModelProperty("管理提成已付 0表示未付 1表示已付")
    private String amountCommissionManagementPaid;

    @ApiModelProperty("项目状态，新建 1  实施 2 完结 3")
    private String proStatus;

    @ApiModelProperty("项目审批人")
    private String proApprover;

    @ApiModelProperty("项目审批时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime proApproveTime;

    @ApiModelProperty("项目创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty("项目创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("项目更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("部门id，用作区分不同的项目权限")
    @TableField(fill = FieldFill.INSERT)
    private Integer departmentId;

}
