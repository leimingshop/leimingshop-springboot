/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <秒杀活动后台设置实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/6
 */
@Data
@ApiModel(description = "SettingSeckillDTO")
public class SettingSeckillDTO {

    @ApiModelProperty(value = "可选活动场次开始时间")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private List<Date> activityStartDate;

    @ApiModelProperty(value = "审核开关 0不需要审核 1需要审核")
    private Integer auditSwitch;

    @ApiModelProperty(value = "销售价显示开关 0不显示 1显示")
    private Integer sellPriceSwitch;

    @ApiModelProperty(value = "预设天数")
    @NotNull(message = "预设天数不能为空")
    @Min(value = 0, message = "预设天数不能小于0天")
    @Max(value = 10, message = "预设天数不能大于10天")
    private Integer presetDays;

    @ApiModelProperty(value = "秒杀活动有效期")
    @NotNull(message = "秒杀活动有效期不能为空")
    @Min(value = 1, message = "秒杀活动有效期不能小于1小时")
    @Max(value = 48, message = "秒杀活动有效期不能大于48小时")
    private Integer activityEffectiveTime;

    @ApiModelProperty(value = "订单支付有效期")
    @NotNull(message = "订单支付有效期不能为空")
    @Min(value = 1, message = "订单支付有效期不能小于1分钟")
    @Max(value = 1440, message = "订单支付有效期不能大于1440分钟")
    private Integer payEffectiveTime;

    @ApiModelProperty(value = "秒杀预约提醒")
    @NotNull(message = "秒杀预约提醒不能为空")
    @Min(value = 1, message = "秒杀预约提醒不能小于1分钟")
    @Max(value = 60, message = "秒杀预约提醒不能大于60分钟")
    private Integer reminderTime;

    @ApiModelProperty(value = "秒杀广告")
    private String advImageUrl;

    @ApiModelProperty(value = "秒杀广告链接url")
    private String advUrl;

}
