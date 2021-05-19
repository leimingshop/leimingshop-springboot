/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("schedule_job")
public class ScheduleJobEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * spring bean名称
     */
    private String beanName;
    /**
     * 参数
     */
    private String params;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 任务状态  0：暂停  1：正常
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
