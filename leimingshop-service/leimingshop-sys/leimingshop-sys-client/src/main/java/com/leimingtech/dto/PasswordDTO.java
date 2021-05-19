/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 修改密码
 *
 * @since 1.0.0
 */
@Data
@ApiModel(description = "PasswordDTO")
public class PasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "原密码")
    @NotBlank(message = "{sysuser.password.require}")
    private String password;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "{sysuser.password.require}")
    private String newPassword;

}