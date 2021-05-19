/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.flashsale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 限时抢购活动信息
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */
@Data
@ApiModel(description = "FlashSaleActivityDTO")
public class FlashSaleActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    @ApiModelProperty(value = "活动开始时间")
    private Date activityStartDate;
    @ApiModelProperty(value = "活动结束时间")
    private Date activityEndDate;
    @ApiModelProperty(value = "活动状态(默认:0未开始，1进行中，2已结束)")
    private Integer activityState;
    @ApiModelProperty(value = "订单支付有效时间(单位：分钟)")
    private Integer payValidTime;
    @ApiModelProperty(value = "会员等级id")
    private Long memberGradeId;
    @ApiModelProperty(value = "会员等级名称")
    private String memberGradeName;
    @ApiModelProperty(value = "会员等级积分")
    private Integer memberGradeLimit;
    @ApiModelProperty(value = "活动限购数量")
    private Integer restrictionQuantity;
    @ApiModelProperty(value = "浏览数")
    private Integer browseNum;
    @ApiModelProperty(value = "下单数")
    private Integer orderNum;
    @ApiModelProperty(value = "积分是否可用(默认：0不可用，1可用)")
    private Integer pointFlag;
    @ApiModelProperty(value = "余额是否可用(默认：0不可用，1可用)")
    private Integer balanceFlag;
    @ApiModelProperty(value = "优惠券是否可用 (默认：0不可用，1可用)")
    private Integer couponsFlag;
    @ApiModelProperty(value = "满减是否可用(默认：0不可用，1可用)")
    private Integer reduceFlag;
}
