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
 * 产品信息表
 *
 * @author syyang
 * @since 2023-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InventoryProductInfo对象")
public class InventoryProductInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品描述")
    private String productDesc;

    @ApiModelProperty("产品类型")
    private String productType;

    @ApiModelProperty("产品型号")
    private String productModel;

    @ApiModelProperty("产品参数")
    private String productParam;

    @ApiModelProperty("单位")
    private String productUnit;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("部门id，用作区分不同的部门权限")
    private Integer departmentId;

}
