/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 售后日志表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_aftersale_log")
public class AftersaleLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 服务单号
     */
    private Long serviceSn;

    /**
     * 售后单号
     */
    private Long aftersaleSn;

    /**
     * 当前售后状态
     */
    private Integer status;

    /**
     * 信息
     */
    private String message;

    /**
     * 日志类型（0:退货退款,1:换货）
     */
    private Integer type;

    /**
     * 当前进度（0:提交申请,1:申请审核中,2:用户取消,3:售后收货,4:换货出库,5:进行退款,6:处理完成,7:审核拒绝,8:仲裁中,9:仲裁拒绝）
     */
    private Integer process;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认0:未删除,1:已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}