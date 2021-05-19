/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.price;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "SpecAttrValueDTO")
public class SpecAttrValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规格属性ID
     */
    @ApiModelProperty(value = "规格属性ID")
    private Long specAttrId;
    /**
     * 规格属性ID
     */
    @ApiModelProperty(value = "规格属性值ID")
    private Long specAttrValueId;

    /**
     * 规格属性值
     */
    @ApiModelProperty(value = "规格属性值")
    private String specAttrValue;


}