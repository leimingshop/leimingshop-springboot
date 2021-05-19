/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.user;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 新增店铺用户表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "SaveStoreUserDTO")
public class SaveStoreUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登陆账号")
    private String account;
    @ApiModelProperty(value = "登陆密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;
    @ApiModelProperty(value = "昵称")
    @NotNull(message = "姓名不能为空", groups = AddGroup.class)
    private String nickname;
    @ApiModelProperty(value = "性别（默认0:保密，1:男，2:女）")
    @NotNull(message = "性别不能为空", groups = AddGroup.class)
    private Integer sex;
    @ApiModelProperty(value = "手机号")
    @Length(max = 11, message = "手机号只能为11位", groups = AddGroup.class)
    private String mobilePhone;
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @ApiModelProperty(value = "启用禁用 0 启用 1 禁用")
    private Integer isEnable;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}