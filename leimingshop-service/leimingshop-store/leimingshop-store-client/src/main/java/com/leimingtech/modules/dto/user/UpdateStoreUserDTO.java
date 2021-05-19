/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.user;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


/**
 * 修改用户信息
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "UpdateStoreUserDTO")
public class UpdateStoreUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "登陆账号")
    private String account;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "性别（默认0:保密，1:男，2:女）")
    private Integer sex;
    @ApiModelProperty(value = "手机号")
    @Length(max = 11, message = "手机号只能为11位", groups = UpdateGroup.class)
    private String mobilePhone;
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @ApiModelProperty(value = "启用禁用 0 启用 1 禁用")
    private Integer isEnable;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "角色表示 0 普通角色 1 超级管理员")
    private Integer roleMark;
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;


}