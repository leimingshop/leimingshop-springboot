/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/7/4 14:44
 * @Description
 **/
@Data
@ApiModel(description = "GoodsSpecAttrValueVO")
public class GoodsSpecAttrValueVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品规格属性值id")
    private Long id;
    @ApiModelProperty(value = "商品规格属性值")
    private String specAttrValue;
}
