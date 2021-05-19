/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 压缩版地址查询
 *
 * @author songwenhao
 * @email ab4856812@163.com
 */
@ApiModel(description = "AreaMiniDTO")
@Data
public class AreaMiniDTO {

    @ApiModelProperty("地区id")
    private Long i;

    @ApiModelProperty("地区名称")
    private String n;

    @ApiModelProperty(value = "当前地区下级的地区集合")
    private List<AreaMiniDTO> c;

}
