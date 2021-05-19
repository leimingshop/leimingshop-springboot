/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.payment;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 提现日志
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_withdrawal_application_log")
public class WithdrawalApplicationLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 申请id
     */
    private Long applyId;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 提现申请进度（1提现申请中,2申请取消,3提现申请驳回,4提现审核通过发放申请中,5发放驳回,6发放通过）；
     */
    private Integer auditStatus;
}
