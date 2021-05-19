/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 压缩版地址信息
 *
 * @author songwenhao
 * @email ab4856812@163.com
 */
@Data
@ApiModel(description = "AreaAllMiniDTO")
public class AreaAllMiniDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前地址版本")
    private Integer version;

    @ApiModelProperty(value = "地址信息")
    private List<AreaMiniDTO> data;
}