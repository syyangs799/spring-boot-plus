package com.syyang.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.syyang.springbootplus.framework.common.entity.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.syyang.springbootplus.framework.core.validator.groups.Update;

/**
 * 字典数据表
 *
 * @author syyang
 * @since 2023-03-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SyDictData对象")
public class SyDictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("字典编码")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("字典排序")
    private Integer sort;

    @NotBlank(message = "字典标签不能为空")
    @ApiModelProperty("字典标签")
    private String label;

    @NotBlank(message = "字典键值不能为空")
    @ApiModelProperty("字典键值")
    private String value;

    @NotBlank(message = "字典类型不能为空")
    @ApiModelProperty("字典类型")
    private String dictType;

    @ApiModelProperty("状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty("颜色类型")
    private String colorType;

    @ApiModelProperty("css 样式")
    private String cssClass;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建者")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新者")
    private String updater;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否删除")
    @TableLogic
    private Boolean deleted;

}
