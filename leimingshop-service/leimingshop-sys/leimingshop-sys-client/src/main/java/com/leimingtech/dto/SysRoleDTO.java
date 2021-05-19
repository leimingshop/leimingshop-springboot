/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色管理
 *
 * @since 1.0.0
 */
@Data
@ApiModel(description = "SysRoleDTO")
public class SysRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "角色名称")
    @Length(max = 30, message = "角色名称必须介于0和30之间", groups = DefaultGroup.class)
    @NotBlank(message = "{sysrole.name.require}", groups = DefaultGroup.class)
    private String name;

    @ApiModelProperty(value = "人数")
    private Integer number;

    @ApiModelProperty(value = "备注")
    @Length(max = 50, message = "角色说明必须介于0和50之间", groups = DefaultGroup.class)
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "菜单ID列表")
    private List<Long> menuIdList;

    @ApiModelProperty(value = "部门ID列表")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "角色标识（默认0普通角色，1超级管理员）超级管理员角色不能删除与修改")
    private Integer roleFlag;

}