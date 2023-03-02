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
 * 日常收入与支出交易流水表
 *
 * @author syyang
 * @since 2023-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InventoryDailyBusiness对象")
public class InventoryDailyBusiness extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("收入与支出自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("交易类型 收入和支出")
    private String type;

    @ApiModelProperty("子类型-字典表-码")
    private String subType;

    @ApiModelProperty("交易子类型-名称")
    private String subTypeName;

    @ApiModelProperty("金额")
    private String amountMoney;

    @ApiModelProperty("税金")
    private String amountTaxes;

    @ApiModelProperty("交易时间")
    private Date businessTime;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("交易状态 0表示创建 1表示提交审批 2表示通过 -1表示未通过")
    private String status;

    @ApiModelProperty("报销人")
    private String submiter;

    @ApiModelProperty("审核人")
    private String approver;

    @ApiModelProperty("审核时间")
    private Date approveTime;

    @ApiModelProperty("创建日期")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("附件名称")
    private String uploadFileName;

    @ApiModelProperty("附件路径")
    private String uploadFilePath;

    @ApiModelProperty("部门id，用作项目权限区分")
    private Integer departmentId;

}
