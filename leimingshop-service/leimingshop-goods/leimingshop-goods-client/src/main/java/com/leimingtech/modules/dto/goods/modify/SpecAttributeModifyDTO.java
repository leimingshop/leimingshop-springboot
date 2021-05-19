/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.modify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SpecAttributeModifyDTO
 * @Description 商品属性修改DTO
 * @Author DY
 * @Date 2019/6/12 10:59
 * @Version 1.0
 **/
@Data
@ApiModel(description = "SpecAttributeModifyDTO")
public class SpecAttributeModifyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;


    @ApiModelProperty(value = "规格属性名称")
    private String specAttrName;
}
