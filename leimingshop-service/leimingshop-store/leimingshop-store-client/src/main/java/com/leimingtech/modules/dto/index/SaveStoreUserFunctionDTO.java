/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-03-16
 */
@Data
@ApiModel(description = "SaveStoreUserFunctionDTO")
public class SaveStoreUserFunctionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户Id")
    private Long userId;
    @ApiModelProperty(value = "角色Id")
    private Long roleId;
    @ApiModelProperty(value = "菜单Id")
    private Long menuId;

}