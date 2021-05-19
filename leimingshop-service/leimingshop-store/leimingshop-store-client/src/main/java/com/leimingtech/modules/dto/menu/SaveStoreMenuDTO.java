/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 新增店铺菜单表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "SaveStoreMenuDTO")
public class SaveStoreMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "菜单路径URL")
    private String menuUrl;
    @ApiModelProperty(value = "父级菜单ID")
    private Long parentId;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "菜单权限标识")
    private String menuPermission;
    @ApiModelProperty(value = "icon图标")
    private String menuIcon;
    @ApiModelProperty(value = "类型   0：菜单   1：按钮")
    private Integer menuType;
    @ApiModelProperty(value = "菜单资源")
    private List<StoreMenuResourceDTO> resourceList;

}