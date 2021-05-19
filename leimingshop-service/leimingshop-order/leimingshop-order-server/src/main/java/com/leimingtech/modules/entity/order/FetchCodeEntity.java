/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子提货码
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_fetch_code")
public class FetchCodeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 店铺名称
     */
    private String storeName;
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
     * 规格ID
     */
    private Long specId;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品货号
     */
    private String goodsSerial;
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
     * 核销截止日期
     */
    private Date expiryDate;
    /**
     * 有效天数（-1为永久有效）
     */
    private Integer validDay;
    /**
     * 电子提货码
     */
    private String fetchCode;
    /**
     * 电子提货码二维码图片地址
     */
    private String fetchCodeImage;
    /**
     * 提货码状态（默认0待核销，1:部分核销，2:已核销，3:已过期）
     */
    private Integer codeStatus;
    /**
     * 已核销数量
     */
    private Integer exchangeNum;
}
