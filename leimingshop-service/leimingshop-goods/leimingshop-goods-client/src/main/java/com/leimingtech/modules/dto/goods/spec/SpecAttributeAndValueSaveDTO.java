/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品规格属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "SpecAttributeAndValueSaveDTO")
public class SpecAttributeAndValueSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "规格属性ID")
    private Long specAttrId;

    @ApiModelProperty(value = "规格属性值ID")
    private Long specAttrValueId;

    @ApiModelProperty(value = "后台使用")
    private Long tempSpecAttrValueId;

    @ApiModelProperty(value = "规格属性名称")
    private String specAttrName;

    @ApiModelProperty(value = "修改操作填写 是否新增 0:原有; 1:新增属性值  2:新增属性和属性值")
    private Integer addType;

    @ApiModelProperty(value = "规格属性值名称")
    private String specAttrValue;

    @ApiModelProperty(value = "是否选中（0未选中，默认1选中）")
    private Integer isSelect;

    @ApiModelProperty(value = "是否为主规格（默认0否，1是）")
    private Integer isMain;


}