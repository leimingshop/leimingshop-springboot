/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.reduce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * front满减活动分页实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "FrontReduceActivityPageDTO")
public class FrontReduceActivityPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "活动规则")
    private String activityDescription;

    @ApiModelProperty(value = "当前生效使用条件金额")
    private BigDecimal limitAmount;

    @ApiModelProperty(value = "当前生效折扣金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "是否选中 0未选中 1已选中")
    private Integer selectFlag;

    @ApiModelProperty(value = "活动规则 0普通满减 1每满减 2阶梯满减")
    private Integer ruleType;

    @ApiModelProperty(value = "积分是否可用 0不可用 1可用")
    private Integer pointFlag;

    @ApiModelProperty(value = "余额是否可用 0不可用 1可用")
    private Integer balanceFlag;

    @ApiModelProperty(value = "优惠券是否可用 0不可用 1可用")
    private Integer couponsFlag;

    @ApiModelProperty(value = "秒杀活动是否可用 0不可用 1可用")
    private Integer seckillFlag;

}
