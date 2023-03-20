/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syyang.inventory.enums;

import com.syyang.springbootplus.framework.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目操作类型
 *
 * @author geekidea
 * @date 2019-10-24
 **/
@Getter
@AllArgsConstructor
public enum ProjectOperationTypeEnum implements BaseEnum {

    /** 项目信息创建 **/
    PROJECT_CREATE_BASE_INFO(0, "项目信息创建"),
    /** 项目信息修改 **/
    PROJECT_UPDATE_BASE_INFO(1, "项目信息修改"),
    /** 项目收支流水创建 **/
    PROJECT_CREATE_BUSINESS_INFO(2, "项目收支流水创建"),
    /** 项目收支流水审核 **/
    PROJECT_UPDATE_BUSINESS_INFO(3, "项目收支流水审核"),
    /** 项目收支流水出纳 **/
    PROJECT_CASHIER_BUSINESS_INFO(5, "项目收支流水出纳"),
    /** 项目出库记录创建 **/
    PROJECT_CREATE_Outbound_INFO(6, "项目出库记录创建"),
    /** 项目出库记录审核 **/
    PROJECT_CHECK_Outbound_INFO(7, "项目出库记录审核");

    private Integer code;
    private String desc;

}
