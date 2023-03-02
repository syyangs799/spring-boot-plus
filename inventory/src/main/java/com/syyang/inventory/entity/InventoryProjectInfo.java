package com.syyang.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.syyang.springbootplus.framework.common.entity.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
    private String name;

    @ApiModelProperty("商务负责人id")
    private String businesserId;

    @ApiModelProperty("商务负责人名称")
    private String businesserName;

    @ApiModelProperty("项目描述")
    private String desc;

    @ApiModelProperty("技术负责人id")
    private Integer technicalerId;

    @ApiModelProperty("技术负责人名称")
    private String technicalerName;

    @ApiModelProperty("合同金额")
    private String amountContract;

    @ApiModelProperty("质保金")
    private String amountWarranty;

    @ApiModelProperty("质保金截止日期")
    private Date amountWarrantyEnding;

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
    private Date proApproveTime;

    @ApiModelProperty("项目创建人")
    private String createUser;

    @ApiModelProperty("项目创建时间")
    private Date createTime;

    @ApiModelProperty("项目更新时间")
    private Date updateTime;

    @ApiModelProperty("部门id，用作区分不同的项目权限")
    private Integer departmentId;

}
