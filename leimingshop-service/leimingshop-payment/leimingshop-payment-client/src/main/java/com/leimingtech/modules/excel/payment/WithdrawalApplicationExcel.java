/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.payment;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现申请表
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Data
public class WithdrawalApplicationExcel {
    @Excel(name = "提现申请编号")
    private Long id;
    @Excel(name = "会员id")
    private Long memberId;
    @Excel(name = "会员名称")
    private String buyerName;
    @Excel(name = "提现金额")
    private BigDecimal withdrawalAmount;
    @Excel(name = "收款账号")
    private String bankAccount;
    @Excel(name = "收款人")
    private String bankPeople;
    @Excel(name = "收款银行")
    private String bankName;
    @Excel(name = "收款人手机号")
    private String bankPhone;
    @Excel(name = "提现申请进度（1审核中，2审核通过，3已驳回，4已取消）")
    private Integer auditStatus;
    @Excel(name = "申请驳回原因")
    private String auditReason;
    @Excel(name = "提现发放id")
    private Long issueId;
    @Excel(name = "创建人")
    private String creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新人")
    private String updater;
    @Excel(name = "更新时间")
    private Date updateDate;

}
