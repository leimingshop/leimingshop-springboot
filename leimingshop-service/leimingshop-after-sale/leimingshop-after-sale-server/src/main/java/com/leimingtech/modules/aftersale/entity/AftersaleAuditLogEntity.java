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
 * 售后审核记录表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_aftersale_audit_log")
public class AftersaleAuditLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 售后单号
     */
    private Long aftersaleSn;

    /**
     * 审核理由
     */
    private String reason;

    /**
     * 审核结果
     */
    private Integer result;

    /**
     * 审核流程（1:商家审核,2:平台审核，3:仲裁审核）
     */
    private Long process;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
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