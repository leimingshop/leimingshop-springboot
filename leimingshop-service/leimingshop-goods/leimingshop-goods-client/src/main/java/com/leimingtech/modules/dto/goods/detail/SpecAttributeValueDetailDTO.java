/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品规格属性值表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "SpecAttributeValueDetailDTO")
public class SpecAttributeValueDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long specAttrValueId;

    @ApiModelProperty(value = "商品规格属性值")
    private String specAttrValue;

    @ApiModelProperty(value = "是否选中（默认0未选中，1选中）")
    private Integer isSelect;

    @ApiModelProperty(value = "是否是主规格属性值（默认0否，1是）")
    private Integer isMainValue;

}