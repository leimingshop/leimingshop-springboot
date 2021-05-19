/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 核销记录entity
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_verify_record")
public class VerifyRecordEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private Long orderSn;
    /**
     * 订单商品ID
     */
    private Long orderGoodsId;
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 商品图片
     */
    private String goodsImage;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer goodsNum;
    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;
    /**
     * 商品货号
     */
    private String goodsSerial;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 用户手机号
     */
    private String memberMobile;
    /**
     * 核销数量
     */
    private Integer exchangeNum;
    /**
     * 电子提货码
     */
    private String fetchCode;
}
