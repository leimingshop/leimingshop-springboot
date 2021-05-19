/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <更多秒杀日期实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/13
 */
@Data
@ApiModel("更多秒杀日期实体")
public class MoreDaysSeckillDTO {

    @ApiModelProperty("活动开始日期")
    private String activityDay;

    @ApiModelProperty("活动开始日期展示")
    private String activityDayStr;

}
