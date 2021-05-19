/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author anjun
 * @email anjun_314914423@126.com
 * @since 1.0.0 2019-05-14
 */
@Data
@ApiModel(description = "AreaAllDTO")
public class AreaAllDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前地址版本")
    private Integer version;

    @ApiModelProperty(value = "一级地址")
    private String firstArea;

    @ApiModelProperty(value = "二级地址")
    private String secondArea;

    @ApiModelProperty(value = "三级地址")
    private String thirdArea;

    @ApiModelProperty(value = "四级地址")
    private String fourthArea;

}