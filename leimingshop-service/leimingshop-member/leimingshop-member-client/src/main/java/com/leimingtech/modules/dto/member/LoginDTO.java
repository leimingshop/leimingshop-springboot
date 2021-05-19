/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录信息
 *
 * @author DY
 * @since 1.0.0
 */
@Data
@ApiModel(description = "LoginDTO")
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "{sysuser.username.require}")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "{sysuser.password.require}")
    private String password;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "{sysuser.captcha.require}")
    private String captcha;
    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识")
    @NotBlank(message = "{sysuser.uuid.require}")
    private String uuid;

}
