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
 * @Date 2019/7/3 18:42
 * @Description 返回规格属性名和属性值的关系
 **/
@Data
@ApiModel(description = "SpecAttrValueRefVO")
public class SpecAttrValueRefVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "规格id")
    private Long specId;

    @ApiModelProperty(value = "商品规格属性ID")
    private Long specAttrId;

    @ApiModelProperty(value = "商品规格属性值ID")
    private Long specAttrValueId;

    @ApiModelProperty(value = "是否主规格属性（0:不是,1:是）")
    private Integer isMain;

    @ApiModelProperty(value = "规格上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer specShow;

    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;

    @ApiModelProperty(value = "是否删除 0 未删除 1已删除")
    private Integer delFlag;


}
