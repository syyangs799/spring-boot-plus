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
    @LogForUpdate(fieldName = "项目名称")
    private String projectName;

    @ApiModelProperty("商务负责人id")
    private Integer businesserId;

    @ApiModelProperty("商务负责人名称")
    @LogForUpdate(fieldName = "商务负责人名称")
    private String businesserName;

    @ApiModelProperty("项目描述")
    @LogForUpdate(fieldName = "项目描述")
    private String projectDesc;

    @ApiModelProperty("技术负责人id")
    private Integer technicalerId;

    @ApiModelProperty("技术负责人名称")
    @LogForUpdate(fieldName = "技术负责人名称")
    private String technicalerName;

    @ApiModelProperty("合同金额")
    @LogForUpdate(fieldName = "合同金额")
    private String amountContract = "0";

    @ApiModelProperty("质保金")
    @LogForUpdate(fieldName = "质保金")
    private String amountWarranty = "0";

    @ApiModelProperty("质保金截止日期")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @LogForUpdate(fieldName = "质保金截止日期")
    private LocalDateTime amountWarrantyEnding;

    @ApiModelProperty("项目利润")
    private String amountProfit = "0";

    @ApiModelProperty("项目纯利润")
    private String amountProfitNet = "0";

    @ApiModelProperty("商务费用")
    @LogForUpdate(fieldName = "商务费用")
    private String amountBusiness = "0";

    @ApiModelProperty("商务费用利率 0-1的百分比")
    @LogForUpdate(fieldName = "商务费用提成")
    private String amountBusinessPaid = "0.15";

    @ApiModelProperty("含税成本")
    private String amountCostTax = "0";

    @ApiModelProperty("不含税成本")
    private String amountCostNoTax = "0";

    @ApiModelProperty("税务成本")
    private String amountTax = "0";

    @ApiModelProperty("税务成本利率 0-1的百分比")
    @LogForUpdate(fieldName = "税务已付成本提成")
    private String amountTaxPaid = "0.15";

    @ApiModelProperty("业务提成")
    private String amountCommissionBusiness = "0";

    @ApiModelProperty("业务提成利率 0-1的百分比")
    @LogForUpdate(fieldName = "业务已付提成提成")
    private String amountCommissionBusinessPaid = "0.15";

    @ApiModelProperty("技术提成")
    private String amountCommissionTechnical = "0";

    @ApiModelProperty("技术提成利率 0-1的百分比")
    @LogForUpdate(fieldName = "技术已付提成提成")
    private String amountCommissionTechnicalPaid = "0.15";

    @ApiModelProperty("管理提成")
    private String amountCommissionManagement = "0";

    @ApiModelProperty("管理提成利率 0-1的百分比")
    @LogForUpdate(fieldName = "管理已付提成提成")
    private String amountCommissionManagementPaid = "0.15";

    @ApiModelProperty("项目进度，新建 1  实施 2 完结 3")
    @LogForUpdate(fieldName = "项目进度")
    private String step;


    @ApiModelProperty("项目审批人")
    private String approver;

    @ApiModelProperty("项目审批人名称")
    private String approverName;

    @ApiModelProperty("项目审批时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime approverTime;

    @ApiModelProperty("项目创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty("项目创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String createUserName;

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



    @ApiModelProperty("项目统计-应收款")
    private String totalReceivables = "0";
    @ApiModelProperty("项目统计-已收款")
    private String totalReceived = "0";
    @ApiModelProperty("项目统计-待收款")
    private String totalUnreceived = "0";
    @ApiModelProperty("项目统计-应支付")
    private String totalPayable = "0";
    @ApiModelProperty("项目统计-已支付")
    private String totalPaid = "0";
    @ApiModelProperty("项目统计-未支付")
    private String totalUnpaid = "0";

}
