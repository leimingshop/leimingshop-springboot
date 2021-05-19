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
@ApiModel(description = "SpecAttrNameDTO")
public class SpecAttrNameDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 规格Id
     */
    @ApiModelProperty(value = "规格Id")
    private Long specId;
    /**
     * 规格属性名称
     */
    @ApiModelProperty(value = "规格属性名称")
    private String specAttrName;
}