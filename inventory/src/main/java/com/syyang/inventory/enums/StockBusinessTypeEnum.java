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
public enum StockBusinessTypeEnum implements BaseEnum {
    IN(0, "入库"),
    OUT(1, "出库");

    private Integer code;
    private String desc;

    StockBusinessTypeEnum(Integer code, String desc) {
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
