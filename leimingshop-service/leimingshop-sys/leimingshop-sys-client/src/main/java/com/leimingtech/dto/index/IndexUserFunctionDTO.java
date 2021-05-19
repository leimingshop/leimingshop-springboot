/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuzhch
 * @className IndexUserFunctionDTO
 * @description 首页用户快捷按钮
 * @date 2020/3/18/018
 */
@Data
@ApiModel(description = "IndexUserFunctionDTO")
public class IndexUserFunctionDTO implements Serializable {
    private Long id;
    @ApiModelProperty("菜单Id")
    private Long menuId;
    @ApiModelProperty("菜单父Id")
    private Long menuPid;
    @ApiModelProperty("菜单类型(0:菜单，1：按钮)")
    private Integer type;
    @ApiModelProperty("排序字段")
    private Integer sort;
    @ApiModelProperty("菜单名称")
    private String menuName;
    @ApiModelProperty("菜单url")
    private String menuUrl;
    @ApiModelProperty("菜单Icon")
    private String menuIcon;
    @ApiModelProperty("菜单权限")
    private String menuPermissions;

}
