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

/**
 * 层级枚举
 * @author geekidea
 * @date 2019-10-24
 **/
public enum StatusTypeEnum implements BaseEnum {
    NEW(0, "新建"),
    CHECKING(1, "提交待审核"),
    CHECK_SUCCESS(2, "审核通过"),
    CHECK_FAILED(-1, "审核失败"),
    CASHI_SUCCESS(3, "出纳"),
    CASHI_FALIED(4, "出纳失败");

    private Integer code;
    private String desc;

    StatusTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
