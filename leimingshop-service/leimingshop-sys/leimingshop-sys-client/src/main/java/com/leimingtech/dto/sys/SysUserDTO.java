/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.sys;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户管理
 *
 * @since 1.0.0
 */
@Data
@ApiModel(description = "SysUserDTO")
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "{sysuser.username.require}", groups = DefaultGroup.class)
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "{sysuser.password.require}", groups = AddGroup.class)
    private String password;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "{sysuser.realname.require}", groups = DefaultGroup.class)
    private String realName;

    @ApiModelProperty(value = "头像")
    private String headUrl;

    @ApiModelProperty(value = "性别   0：男   1：女    2：保密", required = true)
    @Range(min = 0, max = 2, message = "{sysuser.gender.range}", groups = DefaultGroup.class)
    private Integer gender;

    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "{sysuser.email.require}", groups = DefaultGroup.class)
    @Email(message = "{sysuser.email.error}", groups = DefaultGroup.class)
    private String email;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "{sysuser.mobile.require}", groups = DefaultGroup.class)
    private String mobile;

    @ApiModelProperty(value = "部门ID", required = true)
    private Long deptId;

    @ApiModelProperty(value = "超级管理员   0：否   1：是")
    @Range(min = 0, max = 1, message = "{sysuser.superadmin.range}", groups = DefaultGroup.class)
    private Integer superAdmin;

    @ApiModelProperty(value = "状态  0：停用    1：正常", required = true)
    @Range(min = 0, max = 1, message = "{sysuser.status.range}", groups = DefaultGroup.class)
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    /**
     * @update lx 管理员和角色对应关系修改为一对一
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
