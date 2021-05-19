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
 * <优惠券活动ES索引同步实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */
@Data
public class CouponsActivityIndexVo {

    /**
     * ID
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 活动范围 0平台 1店铺
     */
    @FieldInfo(type = "integer")
    private Integer activityScope;

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
     * 店铺logo
     */
    private String storeLogo;

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
     * 领取开始时间
     */
    @ApiModelProperty(value = "领取开始时间")
    private Date getStartDate;

    /**
     * 领取结束时间
     */
    @FieldInfo(type = "date")
    private Date getEndDate;

    /**
     * 有效期类型 0固定时间 1有效天数
     */
    @FieldInfo(type = "integer")
    private Integer validityType;

    /**
     * 使用开始时间
     */
    @FieldInfo(type = "date")
    private Date useStartDate;

    /**
     * 使用结束时间
     */
    @FieldInfo(type = "date")
    private Date useEndDate;

    /**
     * 有效天数
     */
    @FieldInfo(type = "integer")
    private Integer validityDays;

    /**
     * 每人限领数量
     */
    @FieldInfo(type = "long")
    private Long personLimit;

    /**
     * 总数量
     */
    @FieldInfo(type = "long")
    private Long totalNum;

    /**
     * 剩余数量
     */
    @FieldInfo(type = "long")
    private Long surplusNum;

    /**
     * 活动状态 0未开始 1进行中 2已结束
     */
    @FieldInfo(type = "integer")
    private Integer activityState;

    /**
     * 活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌
     */
    @FieldInfo(type = "integer")
    private Integer activityGoodsScope;

    /**
     * 活动商品集合
     */
    @FieldInfo(type = "nested")
    private List<CouponsGoodsDTO> goodsList;

}
