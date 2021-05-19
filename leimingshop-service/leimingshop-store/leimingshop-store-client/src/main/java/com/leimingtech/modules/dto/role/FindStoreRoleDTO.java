/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 查询店铺角色表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "FindStoreRoleDTO")
public class FindStoreRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "备注")
    private String roleRemark;

    @ApiModelProperty(value = "菜单ID列表")
    private List<Long> menuIdList;

}