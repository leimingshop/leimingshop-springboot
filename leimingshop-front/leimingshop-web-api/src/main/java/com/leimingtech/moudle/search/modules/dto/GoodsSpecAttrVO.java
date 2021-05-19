/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author tyl
 * @Date 2019/7/4 14:43
 * @Description
 **/
@Data
@ApiModel(description = "GoodsSpecAttrVO")
public class GoodsSpecAttrVO implements Serializable {

    @ApiModelProperty(value = "商品id")
    private Long id;

    @ApiModelProperty(value = "规格属性名称")
    private String specAttrName;

    @ApiModelProperty(value = "是否选中（默认0未选中，1选中）")
    private Integer isSelect;

    @ApiModelProperty(value = "规格属性id")
    private Long attrSpecId;

    @ApiModelProperty(value = "是否主图（0否，1是）")
    private Integer isMain;

    @ApiModelProperty(value = "规格属性值")
    private String attrValueName;

    @ApiModelProperty(value = "规格属性值关联")
    private List<GoodsSpecAttrValueVO> goodsSpecAttrValueVOList;
}
