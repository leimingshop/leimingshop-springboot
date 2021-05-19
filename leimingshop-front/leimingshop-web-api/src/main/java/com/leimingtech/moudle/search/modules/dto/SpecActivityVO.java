/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <商品详情活动实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/16
 */
@Data
@ApiModel("商品详情活动实体")
public class SpecActivityVO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("秒杀场次id")
    private Long sessionId;

    @ApiModelProperty("活动id")
    private Long activityId;

    @ApiModelProperty("活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty("活动库存")
    private Integer activityStorage;

    @ApiModelProperty("活动剩余库存")
    private Integer activitySurplusStorage;

    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty("活动状态 （0未开始 1进行中 2已结束）")
    private Integer activityState;

    @ApiModelProperty("活动开始时间")
    private Date activityStartDate;

    @ApiModelProperty("活动结束时间")
    private Date activityEndDate;

    @ApiModelProperty("会员等级limit")
    private Integer memberGradeLimit;

    @ApiModelProperty("会员等级id")
    private Long memberGradeId;

    @ApiModelProperty("会员等级名称")
    private String memberGradeName;

    @ApiModelProperty("活动限购数量")
    private Integer restrictionQuantity;

    @ApiModelProperty("拼团成团人数")
    private Integer regimentNum;

    @ApiModelProperty("推荐拼团（0开启，1关闭）")
    private Integer recommendFlag;

    @ApiModelProperty("活动预热时间（小时）")
    private Integer groupPreheat;

    @ApiModelProperty("单次购买件数（仅限制拼团活动）")
    private Integer onceBuyLimit;

    @ApiModelProperty("参团次数限制（仅限制拼团活动）")
    private Integer joinLimit;

    @ApiModelProperty("是否设置提醒 0未设置 1已设置")
    private Integer remindFlag;

}
