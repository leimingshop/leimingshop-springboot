/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodsclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品分类和属性关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-03
 */
@Data
@ApiModel(description = "GoodsClassAttrDTO")
public class GoodsClassAttrDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "商品分类ID")
    private Long gcClassId;
    @ApiModelProperty(value = "属性ID")
    private Long attrId;
    @ApiModelProperty(value = "属性名称")
    private String attrName;
    @ApiModelProperty(value = "属性值")
    private String attrValue;
    @ApiModelProperty(value = "属性分组名称")
    private String attrGroupValue;

}