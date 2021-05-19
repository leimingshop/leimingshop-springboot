/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.vo.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 秒杀专区-秒杀时间段VO
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/16 16:20
 **/
@Data
@ApiModel("秒杀专区-秒杀时间段VO")
public class SeckillTimeSoltVO implements Serializable {
    private static final long serialVersionUID = -242056297442528426L;

    @ApiModelProperty("当前活动场次字符串")
    private String currentDateStr;

    @ApiModelProperty("活动结束时间")
    private Date activityEndDate;

    @ApiModelProperty("秒杀场次id")
    private Long sessionId;

    @ApiModelProperty("即将开始的秒杀时间集合")
    private List<SeckillSoonTimeSoltVO> soonTimeSoltList;
}
