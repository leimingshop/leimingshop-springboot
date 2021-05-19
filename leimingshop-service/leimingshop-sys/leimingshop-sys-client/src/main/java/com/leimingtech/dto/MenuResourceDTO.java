/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单资源
 *
 * @since 1.0.0
 */
@Data
@ApiModel(description = "MenuResourceDTO")
public class MenuResourceDTO {
    @ApiModelProperty(value = "资源URL")
    private String resourceUrl;
    @ApiModelProperty(value = "请求方式（如：GET、POST、PUT、DELETE）")
    private String resourceMethod;

}
