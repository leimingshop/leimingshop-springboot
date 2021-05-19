/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <优惠券活动商品>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/16
 */
@ApiModel(description = "FrontCouponsGoodsVO")
@Data
public class FrontCouponsGoodsVO implements Serializable {

    private static final long serialVersionUID = 6895412133409850237L;

    @ApiModelProperty(value = "skuId")
    private Long id;

    @ApiModelProperty(value = "规格名")
    private String specName;

    @ApiModelProperty(value = "spuId")
    private Long goodsId;

    @ApiModelProperty(value = "售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;

    @ApiModelProperty(value = "销量")
    private Integer specSaleNum;

    @ApiModelProperty(value = "商品信息")
    private GoodsVO goodsVO;

    @ApiModelProperty(value = "商品类型  1 自营商品 2 普通商品")
    private Integer goodsType;
}
