/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.role;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;


/**
 * 修改店铺角色表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "UpdateStoreRoleDTO")
public class UpdateStoreRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private Long id;
    @Length(max = 30, message = "角色名称必须介于0和30之间", groups = UpdateGroup.class)
    private String roleName;
    @ApiModelProperty(value = "备注")
    @Length(max = 50, message = "角色说明必须介于0和50之间", groups = UpdateGroup.class)
    private String roleRemark;
    @ApiModelProperty(value = "角色标识")
    private Integer roleMark;

    @ApiModelProperty(value = "菜单ID列表")
    private List<Long> menuIdList;

}