/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Data: 2019/12/26 10:53
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel(description = "OrderBillSettingDTO")
public class OrderBillSettingDTO implements Serializable {

    @ApiModelProperty("对账周期类型(1 天数，2 周，3 月)")
    private Integer type;

    @ApiModelProperty("对账日期")
    private String days;

    @ApiModelProperty("对账周期开始时间")
    private Date startTime;

    @ApiModelProperty("对账周期结束时间")
    private Date endTime;

    @ApiModelProperty("生效时间")
    private Date createDate;
}
