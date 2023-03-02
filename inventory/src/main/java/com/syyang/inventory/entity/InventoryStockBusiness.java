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
 * 库存交易流水表
 *
 * @author syyang
 * @since 2023-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InventoryStockBusiness对象")
public class InventoryStockBusiness extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("产品id")
    private String productId;

    @ApiModelProperty("交易类型，1入库 2出库")
    private String type;

    @ApiModelProperty("产品数量")
    private Integer productNum;

    @ApiModelProperty("产品金额-小计")
    private String productAmount;

    @ApiModelProperty("产品厂家")
    private String productManufactor;

    @ApiModelProperty("产品单价")
    private String productPrice;

    @ApiModelProperty("产品税金")
    private String productTax;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("当前字段为出库时必填，入库的产品id")
    private Integer bachId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("部门id，用作区分不同的项目id")
    private Integer departmentId;

}
