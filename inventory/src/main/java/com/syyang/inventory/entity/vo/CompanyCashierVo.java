package com.syyang.inventory.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: yyangs
 * @CreateDate: 2023/3/22 23:07
 * @UpdateDate: 2023/3/22 23:07
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu
 */
@Data
public class CompanyCashierVo implements Serializable {

    private static final long serialVersionUID = -1;

    /**
     * shouru
     */
    private String shouru;

    /**
     * zhichu
     */
    private String zhichu;
    /**
     * riqi
     */
    private String riqi;

}
