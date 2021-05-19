/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 立即购买封装商品实体
 * @Date: 11:52 2019/6/17
 * @Version: V1.0
 */
@Data
@ToString
public class GoodsToOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车集合
     */
//    @ApiModelProperty(value = "购物车集合")
//    private List<CartDTO> cartDTOList;

    /**
     * 商品总数量
     */
    @ApiModelProperty(value = "商品总数量")
    private Integer goodsNumber;

    /**
     * 商品总价格
     */
    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsTotalPrice;

    /**
     * 商品总运费
     */
    @ApiModelProperty(value = "商品总运费")
    private String goodsTotalFreight;

    /**
     * 商品店铺id
     */
    @ApiModelProperty(value = "商品店铺id")
    private Long storeId;

    /**
     * 商品店铺名称
     */
    @ApiModelProperty(value = "商品店铺名称")
    private String storeName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "商品店铺图标")
    private String storeLogo;

}
