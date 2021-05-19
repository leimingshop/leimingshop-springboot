/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单统计
 *
 * @author xuzhch
 * @date 2019年12月10日
 */

@Data
public class OrderStatisDTO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单商品数量
     */
    private Integer goodsNum;
    /**
     * 订单实付金额
     */
    private BigDecimal orderAmount;
    /**
     * 订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;
     */
    private Integer orderStatus;
    /**
     * 订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;
     */
    private Integer orderSource;
    /**
     * 付款状态:0:未付款;1:已付款
     */
    private Integer paymentStatus;
    /**
     * 创建时间（日）
     */
    private Date createDayTime;
    /**
     * 创建时间（月）
     */
    private Date createMonthTime;
    /**
     * 创建时间
     */
    private Date createTime;

}
