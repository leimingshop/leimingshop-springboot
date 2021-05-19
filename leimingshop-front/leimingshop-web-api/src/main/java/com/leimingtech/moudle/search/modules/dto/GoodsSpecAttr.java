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
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/7/29 11:41
 * @Version: V1.0
 */
@Data
@ApiModel(description = "GoodsSpecAttr")
public class GoodsSpecAttr implements Serializable {

    @ApiModelProperty(value = "商品id")
    private Long id;

    @ApiModelProperty(value = "规格属性名称")
    private String specAttrName;

    @ApiModelProperty(value = "是否选中（默认0未选中，1选中）")
    private Integer isSelect;

    @ApiModelProperty(value = "是否主图（0否，1是）")
    private Integer isMain;

    @ApiModelProperty(value = "规格属性值")
    private String attrValueName;

    @ApiModelProperty(value = "规格属性值关联")
    private List<GoodsSpecAttrValueVO> goodsSpecAttrValueVOList;
}
