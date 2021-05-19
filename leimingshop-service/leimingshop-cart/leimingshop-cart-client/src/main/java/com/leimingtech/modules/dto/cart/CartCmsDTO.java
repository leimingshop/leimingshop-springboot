/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 购物车表
 *
 * @author 余海锋
 * @since 1.0.0 2020-04-14
 */
@Data
@ApiModel(description = "CartDTO")
public class CartCmsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
}
