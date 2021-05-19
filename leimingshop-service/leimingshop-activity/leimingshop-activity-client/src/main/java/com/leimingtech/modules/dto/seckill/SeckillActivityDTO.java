/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 秒杀活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(value = "秒杀活动实体")
public class SeckillActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "场次id")
    private Long sessionId;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    @ApiModelProperty(value = "活动开始时间")
    private Date activityStartDate;
    @ApiModelProperty(value = "活动结束时间")
    private Date activityEndDate;
    @ApiModelProperty(value = "活动有效时间")
    private Integer activityEffectiveTime;
    @ApiModelProperty(value = "会员等级id")
    private Long memberGradeId;
    @ApiModelProperty(value = "会员等级名称")
    private String memberGradeName;
    @ApiModelProperty(value = "会员等级id")
    private Integer memberGradeLimit;
    @ApiModelProperty(value = "活动限购数量")
    private Integer restrictionQuantity;
    @ApiModelProperty(value = "浏览数")
    private Integer browseNum;
    @ApiModelProperty(value = "下单数")
    private Integer orderNum;
    @ApiModelProperty(value = "审核状态 0未审核 1审核通过 2审核拒绝")
    private Integer auditState;
    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;
    @ApiModelProperty(value = "积分是否可用 0不可用 1可用")
    private Integer pointFlag;
    @ApiModelProperty(value = "余额是否可用 0不可用 1可用")
    private Integer balanceFlag;
    @ApiModelProperty(value = "优惠券是否可用 0不可用 1可用")
    private Integer couponsFlag;
    @ApiModelProperty(value = "满减是否可用 0不可用 1可用")
    private Integer reduceFlag;
    @ApiModelProperty(value = "满包邮是否可用 0不可用 1可用")
    private Integer freeShippingFlag;
}
