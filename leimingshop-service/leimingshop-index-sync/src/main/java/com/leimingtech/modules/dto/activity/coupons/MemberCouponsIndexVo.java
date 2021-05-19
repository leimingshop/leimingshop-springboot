/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.coupons;

import com.leimingtech.modules.dto.coupons.CouponsGoodsDTO;
import com.leimingtech.modules.utils.FieldInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <会员优惠券ES索引同步实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */
@Data
public class MemberCouponsIndexVo {

    /**
     * ID
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 优惠券活动id
     */
    @FieldInfo(type = "long")
    private Long activityId;

    /**
     * 会员id
     */
    @FieldInfo(type = "long")
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 店铺ID
     */
    @FieldInfo(type = "long")
    private Long storeId;

    /**
     * 店铺名称
     */
    @FieldInfo(participle = 0)
    private String storeName;

    /**
     * 优惠券类型 0满减券 1满折券
     */
    @FieldInfo(type = "integer")
    private Integer couponsType;

    /**
     * 使用限制金额
     */
    @FieldInfo(type = "float")
    private BigDecimal limitAmount;

    /**
     * 优惠券面额
     */
    @FieldInfo(type = "float")
    private BigDecimal faceValue;

    /**
     * 优惠券状态 未使用(0不可使用 1可使用) 2已使用 3已过期
     */
    @FieldInfo(type = "integer")
    private Integer couponsState;

    /**
     * 使用开始时间
     */
    @FieldInfo(type = "date")
    private Date startDate;

    /**
     * 使用结束时间
     */
    @FieldInfo(type = "date")
    private Date endDate;

    /**
     * 使用时间
     */
    @FieldInfo(type = "date")
    private Date useDate;

    /**
     * 领取时间
     */
    @FieldInfo(type = "date")
    private Date createDate;

    /**
     * 订单编号
     */
    @FieldInfo(type = "long")
    private Long orderSn;

    /**
     * 订单id
     */
    @FieldInfo(type = "long")
    private Long orderId;

    /**
     * 活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌
     */
    @FieldInfo(type = "integer")
    @ApiModelProperty("活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    /**
     * 活动商品集合
     */
    @FieldInfo(type = "nested")
    private List<CouponsGoodsDTO> goodsList;

}
