/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 拼团
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(description = "GroupDTO")
public class GroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "拼团名称")
    private String groupName;
    @ApiModelProperty(value = "拼团开始时间")
    private Date startTime;
    @ApiModelProperty(value = "拼团结束时间")
    private Date endTime;
    @ApiModelProperty(value = "参团条件（默认0无限制，1新用户）")
    private Integer joinFlag;
    @ApiModelProperty(value = "活动预热时间（小时）")
    private Integer groupPreheat;
    @ApiModelProperty(value = "推荐拼团（0开启，1关闭）")
    private Integer recommendFlag;
    @ApiModelProperty(value = "模拟成团（0开启，1关闭）")
    private Integer simulateFlag;
    @ApiModelProperty(value = "成团有效时间（小时）")
    private Integer validTime;
    @ApiModelProperty(value = "订单支付有效期（分钟）")
    private Integer payEndTime;
    @ApiModelProperty(value = "下单使用优惠（0允许，默认1不允许）")
    private Integer discountFlag;
    @ApiModelProperty(value = "下单可用抵扣（（0允许，默认1不允许））")
    private Integer deductionFlag;
    @ApiModelProperty(value = "审核状态（10：发布 20：审核 30：审核通过 40：审核未通过 50：取消）")
    private Integer auditStatus;
    @ApiModelProperty(value = "拼团众筹活动状态 0：未开始 1：活动中 2：活动结束")
    private Integer activityStatus;
    @ApiModelProperty(value = "商品个数（待定）")
    private Integer goodsNum;
    @ApiModelProperty(value = "成团个数（待定）")
    private Integer groupNum;
    @ApiModelProperty(value = "支付订单数（待定）")
    private Integer paymentNum;
}
