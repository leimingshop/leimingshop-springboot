/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


/**
 * 注册店铺账号
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-12-18
 */
@Data
@ApiModel(description = "RegisterStoreDTO")
public class RegisterStoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty(value = "登陆账号")
    private String account;
    @ApiModelProperty(value = "登陆密码")
    private String password;
    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;
    @ApiModelProperty(value = "手机号")
    @Length(max = 11, message = "手机号只能为11位", groups = AddGroup.class)
    private String mobilePhone;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty("性别（默认0:保密，1:男，2:女）")
    private Integer sex;

}