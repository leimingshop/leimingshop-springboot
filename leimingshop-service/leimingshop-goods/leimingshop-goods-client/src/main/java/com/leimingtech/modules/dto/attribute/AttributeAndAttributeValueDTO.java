/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attribute;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 属性及属性值实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Data
@ApiModel(description = "AttributeAndAttributeValueDTO")
public class AttributeAndAttributeValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "属性值列（所有属性值组合，中间用“,”隔开）")
    private String specValue;

    @ApiModelProperty(value = "属性值")
    private List<AttributeValueDTO> attributeValueDTOList;

}
