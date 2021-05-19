/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.vo.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀即将开始时间段VO
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/16 16:24
 **/
@Data
@ApiModel("秒杀即将开始时间段VO")
public class SeckillSoonTimeSoltVO implements Serializable {
    private static final long serialVersionUID = 7092013759071419893L;

    @ApiModelProperty("秒杀场次id")
    private Long sessionId;

    @ApiModelProperty("活动开始时间")
    private Date activityStartDate;

    @ApiModelProperty("活动开始时间展示")
    private String activityStartDateStr;
}
