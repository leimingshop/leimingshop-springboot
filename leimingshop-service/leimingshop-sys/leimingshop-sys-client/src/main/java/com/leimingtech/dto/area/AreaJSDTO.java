/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <导出地区js文件地区实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/7/18
 */
@ApiModel(description = "AreaJSDTO")
@Data
public class AreaJSDTO {

    @ApiModelProperty("地区id")
    private Long i;

    @ApiModelProperty("地区名称")
    private String n;

    @ApiModelProperty("父级地区id")
    private Long p;
}
