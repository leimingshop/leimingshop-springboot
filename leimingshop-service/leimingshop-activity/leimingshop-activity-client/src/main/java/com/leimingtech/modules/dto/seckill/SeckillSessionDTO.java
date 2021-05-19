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
 * 秒杀场次实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-06
 */
@Data
@ApiModel(value = "秒杀场次实体")
public class SeckillSessionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "活动场次开始时间")
    private Date activityStartDate;

    @ApiModelProperty(value = "活动场次结束时间")
    private Date activityEndDate;

    @ApiModelProperty(value = "活动场次有效时间")
    private Integer activityEffectiveTime;

    @ApiModelProperty(value = "支付有效时间")
    private Integer payEffectiveTime;

    @ApiModelProperty(value = "预约提醒时间")
    private Integer reminderTime;

    @ApiModelProperty(value = "审核开关 0不需要审核 1需要审核")
    private Integer auditSwitch;

    @ApiModelProperty(value = "销售价显示开关 0不显示 1显示")
    private Integer sellPriceSwitch;

}
