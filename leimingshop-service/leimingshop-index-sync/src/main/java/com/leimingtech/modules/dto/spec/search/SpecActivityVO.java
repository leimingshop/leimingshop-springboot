/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <规格活动数据>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/11
 */
@Data
public class SpecActivityVO {

    /**
     * ID
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 秒杀场次id
     */
    @FieldInfo(type = "long")
    private Long sessionId;

    /**
     * 活动id
     */
    @FieldInfo(type = "long")
    private Long activityId;

    /**
     * 活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购
     */
    @FieldInfo(type = "integer")
    private Integer activityType;

    /**
     * 活动库存
     */
    @FieldInfo(type = "integer")
    private Integer activityStorage;

    /**
     * 活动剩余库存
     */
    @FieldInfo(type = "integer")
    private Integer activitySurplusStorage;

    /**
     * 活动价格
     */
    @FieldInfo(type = "float")
    private BigDecimal activityPrice;

    /**
     * 活动状态 （0未开始 1进行中 2已结束）
     */
    @FieldInfo(type = "integer")
    private Integer activityState;

    /**
     * 活动开始时间
     */
    @FieldInfo(type = "date")
    private Date activityStartDate;

    /**
     * 活动结束时间
     */
    @FieldInfo(type = "date")
    private Date activityEndDate;

    /**
     * 会员等级limit
     */
    @FieldInfo(type = "integer")
    private Integer memberGradeLimit;

    /**
     * 会员等级id
     */
    @FieldInfo(type = "long")
    private Long memberGradeId;

    /**
     * 会员等级名称
     */
    @FieldInfo
    private String memberGradeName;

    /**
     * 活动限购数量
     */
    @FieldInfo(type = "integer")
    private Integer restrictionQuantity;

    /**
     * 拼团成团人数
     */
    @FieldInfo(type = "integer")
    private Integer regimentNum;

    /**
     * 推荐拼团（0开启，1关闭）
     */
    @FieldInfo(type = "integer")
    private Integer recommendFlag;

    /**
     * 活动预热时间（小时）
     */
    @FieldInfo(type = "integer")
    private Integer groupPreheat;

    /**
     * 单次购买件数（仅限制拼团活动）
     */
    @FieldInfo(type = "integer")
    private Integer onceBuyLimit;

    /**
     * 参团次数限制（仅限制拼团活动）
     */
    @FieldInfo(type = "integer")
    private Integer joinLimit;

    /**
     * 成团有效时间（小时）
     */
    @FieldInfo(type = "integer")
    private Integer validTime;

}
