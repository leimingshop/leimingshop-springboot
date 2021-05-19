/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 订单物流进度
 * @Date: 2019/8/14 21:38
 * @Version: V1.0
 */
@ApiModel(description = "LogisticsProcessDTO")
@Data
public class LogisticsProcessDTO {

    @ApiModelProperty(value = "物流详细信息")
    private String context;
    @ApiModelProperty(value = "物流进度时间")
    private String time;
    @ApiModelProperty(value = "物流进度时间（格式化后）")
    private String ftime;

}

