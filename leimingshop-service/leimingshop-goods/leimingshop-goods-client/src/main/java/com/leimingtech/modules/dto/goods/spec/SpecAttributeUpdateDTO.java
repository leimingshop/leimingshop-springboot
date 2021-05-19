/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 商品规格属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "SpecAttributeUpdateDTO")
public class SpecAttributeUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "规格属性名称")
    private String specAttrName;

    @ApiModelProperty(value = "是否选中（默认0未选中，1选中）")
    private Integer isSelect;

    @ApiModelProperty(value = "是否为主规格（默认0否，1是）")
    private Integer isMain;

    @ApiModelProperty(value = "商品规格属性值修改对象集合")
    private List<SpecAttributeValueUpdateDTO> specAttributeValueUpdateDTOList;

}