/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 订单确认价钱DTO
 * @Date: 15:33 2019/6/22
 * @Version: V1.0
 */
@Data
@ToString
public class OrderConfirmPriceDTO implements Serializable {

    private static final long serialVersionUID = 2704626024597264723L;

    /**
     * 商品总金额
     */
    private BigDecimal goodsAmout;

    /**
     * 优惠总金额
     */
    private BigDecimal preferentialPrice;

    /**
     * 支付总金额
     */
    private BigDecimal orderAmount;

    /**
     * 运费总金额
     */
    private BigDecimal shippingFee;

}
