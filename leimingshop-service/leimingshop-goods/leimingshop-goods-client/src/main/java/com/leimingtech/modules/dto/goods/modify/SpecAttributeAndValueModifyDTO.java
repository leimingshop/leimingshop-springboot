/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.modify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SpecAttributeAndValueModifyDTO
 * @Description 商品规格属性值移除表
 * @Author DY
 * @Date 2019/6/12 11:08
 * @Version 1.0
 **/
@Data
@ApiModel(description = "SpecAttributeAndValueModifyDTO")
public class SpecAttributeAndValueModifyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品规格属性ID")
    private Long specAttrId;

    @ApiModelProperty(value = "商品规格属性值")
    private String specAttrValue;
}
