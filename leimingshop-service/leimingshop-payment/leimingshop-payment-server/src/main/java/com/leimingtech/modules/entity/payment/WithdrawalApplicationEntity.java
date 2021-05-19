/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.payment;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现申请
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_withdrawal_application")
public class WithdrawalApplicationEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 会员名称
     */
    private String buyerName;
    /**
     * 提现金额
     */
    private BigDecimal withdrawalAmount;
    /**
     * 收款账号
     */
    private String bankAccount;
    /**
     * 收款人
     */
    private String bankPeople;
    /**
     * 收款银行
     */
    private String bankName;
    /**
     * 收款人手机号
     */
    private String bankPhone;
    /**
     * 提现申请进度（1审核中，2审核通过，3已驳回，4已取消）
     */
    private Integer auditStatus;
    /**
     * 申请驳回原因
     */
    private String auditReason;
    /**
     * 提现发放id
     */
    private Long issueId;

    /**
     * 支付时间
     */
    private Date payTime;

}
