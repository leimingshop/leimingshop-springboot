/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.rolemenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺角色菜单管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "StoreRoleMenuDTO")
public class StoreRoleMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

}