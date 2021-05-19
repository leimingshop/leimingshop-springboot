/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.auditlog;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审核记录表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_audit_log")
public class AuditLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 操作内容ID
     */
    private Long contentId;
    /**
     * 操作类型(1 提交，2 审核)
     */
    private Integer submitType;
    /**
     * 审核类型(1 店铺普通信息 2 店铺公司信息 3 店铺入住审核 )
     */
    private Integer auditType;
    /**
     * 审核结果(10 提交申请 20 审核通过 30审核拒绝 )
     */
    private Integer auditStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 操作人
     */
    private String operator;
}