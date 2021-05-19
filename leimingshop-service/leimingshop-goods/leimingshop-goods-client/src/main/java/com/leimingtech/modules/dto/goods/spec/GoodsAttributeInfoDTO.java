/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsAttributeInfoDTO")
public class GoodsAttributeInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "属性ID")
    private Long attributeId;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "属性值（多个属性值之间用逗号隔开）")
    private String attrValue;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "展示类型（默认0单选，1下拉框，2多选）")
    private Integer showType;


}