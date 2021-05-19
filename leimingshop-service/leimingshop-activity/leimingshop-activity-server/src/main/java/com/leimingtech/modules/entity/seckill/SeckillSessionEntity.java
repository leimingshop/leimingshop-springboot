/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.seckill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 秒杀场次实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_seckill_session")
public class SeckillSessionEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 活动场次开始时间
     */
    private Date activityStartDate;

    /**
     * 活动场次结束时间
     */
    private Date activityEndDate;

    /**
     * 活动场次有效时间
     */
    private Integer activityEffectiveTime;

    /**
     * 支付有效时间
     */
    private Integer payEffectiveTime;

    /**
     * 预约提醒时间
     */
    private Integer reminderTime;

    /**
     * 审核开关 0不需要审核 1需要审核
     */
    private Integer auditSwitch;

    /**
     * 销售价显示开关 0不显示 1显示
     */
    private Integer sellPriceSwitch;
}
