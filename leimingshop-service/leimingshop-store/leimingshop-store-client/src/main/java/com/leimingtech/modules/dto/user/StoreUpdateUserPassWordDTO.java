/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺用户表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "StoreUpdateUserPassWordDTO")
public class StoreUpdateUserPassWordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登陆密码")
    private String password;
    @ApiModelProperty(value = "新密码")
    private String newPassword;
    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;


}
