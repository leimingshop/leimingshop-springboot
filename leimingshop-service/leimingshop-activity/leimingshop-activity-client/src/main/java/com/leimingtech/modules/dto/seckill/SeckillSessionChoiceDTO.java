/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import com.leimingtech.commons.tools.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 秒杀场次选择实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-11
 */
@Data
@ApiModel(value = "秒杀场次选择实体")
public class SeckillSessionChoiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "活动场次开始时间")
    private Date activityStartDate;

    @ApiModelProperty(value = "活动场次开始时间展示")
    private Date activityStartDateStr;
    @ApiModelProperty(value = "活动场次结束时间")
    private Date activityEndDate;
    @ApiModelProperty(value = "活动场次结束时间展示")
    private Date activityEndDateStr;
    @ApiModelProperty(value = "活动场次有效时间")
    private Integer activityEffectiveTime;
    @ApiModelProperty(value = "支付有效时间")
    private Integer payEffectiveTime;

    public String getActivityStartDateStr() {
        if (activityStartDate != null) {
            return DateUtils.format(activityStartDate, "yyyy-MM-dd HH:mm");
        }
        return "";
    }

    public String getActivityEndDateStr() {
        if (activityEndDate != null) {
            return DateUtils.format(activityEndDateStr, "yyyy-MM-dd HH:mm");
        }
        return "";
    }

}
