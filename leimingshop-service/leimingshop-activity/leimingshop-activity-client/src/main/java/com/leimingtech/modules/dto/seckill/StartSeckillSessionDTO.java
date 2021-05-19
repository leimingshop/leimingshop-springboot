/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <正在秒杀场次信息实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/13
 */
@Data
@ApiModel("正在秒杀场次信息实体")
public class StartSeckillSessionDTO {

    @ApiModelProperty("广告图片url")
    private String advImageUrl;

    @ApiModelProperty("广告跳转url")
    private String advUrl;

    @ApiModelProperty("秒杀场次id")
    private Long sessionId;

    @ApiModelProperty("活动开始时间")
    private Date activityStartDate;

    @ApiModelProperty("活动结束时间")
    private Date activityEndDate;

}
