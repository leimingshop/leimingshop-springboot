/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 资源管理
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-17
 */
@Data
@ApiModel(description = "StoreResourceDTO")
public class StoreResourceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "资源编码，如菜单ID")
    private String resourceCode;
    @ApiModelProperty(value = "资源名称")
    private String resourceName;
    @ApiModelProperty(value = "资源URL")
    private String resourceUrl;
    @ApiModelProperty(value = "请求方式（如：GET、POST、PUT、DELETE）")
    private String resourceMethod;
    @ApiModelProperty(value = "菜单标识  0：非菜单资源   1：菜单资源")
    private Integer menuFlag;
    @ApiModelProperty(value = "认证等级   0：权限认证   1：登录认证    2：无需认证")
    private Integer authLevel;
}