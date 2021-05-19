/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.favorites;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户收藏查询，新增的时候不需要传id，但是查询的时候要返回id，所以新建了这个DTO供查询的时候使用
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Data
@ApiModel(description = "FavoritesQueryDTO")
public class FavoritesQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收藏id")
    private Long id;

    @ApiModelProperty(value = "商品id", required = true)
    private Long goodsId;

    @ApiModelProperty(value = "商品规格id", required = true)
    private Long specId;

    @ApiModelProperty(value = "收藏时的商品价格", required = true)
    private BigDecimal favPrice;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty("会员ID")
    private Long memberId;
}
