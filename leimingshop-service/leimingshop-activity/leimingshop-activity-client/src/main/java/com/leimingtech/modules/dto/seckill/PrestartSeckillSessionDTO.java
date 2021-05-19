/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import com.leimingtech.commons.tools.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <即将秒杀场次信息实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/13
 */
@Data
@ApiModel("即将秒杀场次信息实体")
public class PrestartSeckillSessionDTO {

    @ApiModelProperty("秒杀场次id")
    private Long sessionId;

    @ApiModelProperty("活动开始时间")
    private Date activityStartDate;

    @ApiModelProperty("活动开始时间展示")
    private String activityStartDateStr;

    public String getActivityStartDateStr() {
        if (activityStartDate != null) {
            return DateUtils.format(activityStartDate, "HH:mm");
        } else {
            return null;
        }
    }

}
