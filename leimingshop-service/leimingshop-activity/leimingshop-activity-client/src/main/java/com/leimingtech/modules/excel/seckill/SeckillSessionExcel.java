/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.seckill;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 秒杀场次实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-06
 */
@Data
public class SeckillSessionExcel {
    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "活动场次开始时间")
    private Date activityStartDate;

    @ExcelProperty(value = "活动场次结束时间")
    private Date activityEndDate;

    @ExcelProperty(value = "活动场次有效时间")
    private Integer activityEffectiveTime;

    @ExcelProperty(value = "支付有效时间")
    private Integer payEffectiveTime;

    @ExcelProperty(value = "预约提醒时间")
    private Integer reminderTime;

    @ExcelProperty(value = "审核开关 0不需要审核 1需要审核")
    private Integer auditSwitch;

    @ExcelProperty(value = "销售价显示开关 0不显示 1显示")
    private Integer sellPriceSwitch;

    @ExcelProperty(value = "创建人")
    private String creator;

    @ExcelProperty(value = "创建时间")
    private Date createDate;

    @ExcelProperty(value = "更新人")
    private String updater;

    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
