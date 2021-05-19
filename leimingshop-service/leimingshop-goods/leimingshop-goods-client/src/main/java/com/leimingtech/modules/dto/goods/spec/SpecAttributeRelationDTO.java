/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品规格属性与属性值关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "SpecAttributeRelationDTO")
public class SpecAttributeRelationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品规格ID")
    private Long specId;
    @ApiModelProperty(value = "商品规格属性ID")
    private Long specAttrId;
    @ApiModelProperty(value = "商品规格属性值ID")
    private Long specAttrValueId;
    @ApiModelProperty(value = "是否为主规格（0否，1是）")
    private Integer isMain;

}