/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * admin订单活动明细
 *
 * @author 刘远杰
 * @since v1.0.0 2020/02/24
 */
@Data
@ApiModel(description = "AdminOrderActivityDTO")
public class AdminOrderActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "活动类型 活动类型 1优惠券 2满减")
    private Integer activityType;

    @ApiModelProperty(value = "使用限制金额")
    private BigDecimal limitAmount;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty(value = "优惠券活动名称")
    private String activityName;

    @ApiModelProperty("活动规则名称")
    private String activityRuleName;

    @ApiModelProperty(value = "活动开始时间")
    private Date startDate;

    @ApiModelProperty(value = "活动结束时间")
    private Date endDate;

}
