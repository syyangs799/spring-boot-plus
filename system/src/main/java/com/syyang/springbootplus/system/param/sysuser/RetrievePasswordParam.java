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

package com.syyang.springbootplus.system.param.sysuser;

import com.syyang.springbootplus.framework.core.validator.groups.Add;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 管理员重置用户密码参数
 *
 * @author geekidea
 * @date 2020-3-8
 **/
@Data
@Accessors(chain = true)
@ApiModel("用户找回密码参数")
public class RetrievePasswordParam implements Serializable {

    private static final long serialVersionUID = 5364321420976152005L;

    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不能为空", groups = {Add.class})
    private String username;

    @ApiModelProperty("手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "确认密码不能为空")
    private String confirmPassword;

}
