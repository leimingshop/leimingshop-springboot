/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活动商品实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(value = "活动商品实体")
public class ActivityGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "秒杀场次id")
    private Long sessionId;
    @ApiModelProperty(value = "活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;
    @ApiModelProperty(value = "商品spu id")
    private Long goodsId;
    @ApiModelProperty(value = "商品sku id")
    private Long specId;
    @ApiModelProperty(value = "活动库存")
    private Integer activityStorage;
    @ApiModelProperty(value = "活动剩余库存")
    private Integer activitySurplusStorage;
    @ApiModelProperty(value = "活动价格")
    private BigDecimal activityPrice;
    @ApiModelProperty(value = "下单数")
    private Integer orderNum;
    @ApiModelProperty(value = "spu订单数（包含已删除的sku下单数）")
    private Integer spuOrderNum;

    @ApiModelProperty(value = "单次购买件数（仅限制拼团活动）")
    private Integer onceBuyLimit;

    @ApiModelProperty(value = "参团次数限制（仅限制拼团活动）")
    private Integer joinLimit;

    @ApiModelProperty(value = "成团人数（仅限制拼团活动）")
    private Integer regimentNum;
    @ApiModelProperty(value = "排序")
    private Integer sort;
}
