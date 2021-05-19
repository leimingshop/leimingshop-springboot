/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改购物车数量
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ApiModel(description = "FavoriteCartDTO")
public class FavoriteCartDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品规格ID")
    private Long specId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "规格销售价")
    private BigDecimal specSellPrice;

}
