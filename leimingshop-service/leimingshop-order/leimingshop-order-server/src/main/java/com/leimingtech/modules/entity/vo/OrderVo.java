/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.vo;

import com.leimingtech.modules.dto.order.OrderGoodsConfirmDTO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 生成订单使用的订单超类 （最新适用）
 * @Date: 23:38 2019/6/20
 * @Version: V1.0
 */
@Data
@ToString
public class OrderVo implements Serializable {

    /**
     * 订单商品确认集合
     */
    private List<OrderGoodsConfirmDTO> list;

    /**
     * 商品总数量
     */
    private int goodsNum;

    /**
     * 商品总价格
     */
    private BigDecimal goodsAmount;

    /**
     * 订单运费
     */
    private BigDecimal shippingFee;

    /**
     * 商品店铺id
     */
    private Long storeId;

    /**
     * 商品店铺名称
     */
    private String storeName;

    /**
     * 订单总价(应付金额)
     */
    private BigDecimal orderAmount;

    /**
     * 商家备注等级
     */
    private Integer sellerRemarkGrade;
    /**
     * 商家备注
     */
    private String sellerRemark;
    /**
     * 买家等级ID
     */
    private Long buyerGraderId;
    /**
     * 买家等级名称
     */
    private String buyerGraderName;

}
