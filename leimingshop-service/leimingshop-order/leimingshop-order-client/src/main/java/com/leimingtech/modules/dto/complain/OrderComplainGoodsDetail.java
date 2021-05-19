/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.complain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 投诉商品信息
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "投诉商品信息")
public class OrderComplainGoodsDetail implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "商品图片")
    private String goodsImage;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品数量")
    private String goodsNum;
}