/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


/**
 * 商品属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsAttributeSaveDTO")
public class GoodsAttributeSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "属性ID")
    private Long attributeId;

    @Length(max = 20, message = "属性名称长度不可超过20", groups = AddGroup.class)
    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @Length(max = 20, message = "属性值长度不可超过20", groups = AddGroup.class)
    @ApiModelProperty(value = "属性值（多个属性值之间用逗号隔开）")
    private String attrValue;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;


}