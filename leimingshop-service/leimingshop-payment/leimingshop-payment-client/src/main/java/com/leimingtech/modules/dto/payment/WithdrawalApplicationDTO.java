/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现申请
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Data
@ApiModel(value = "提现申请")
public class WithdrawalApplicationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现申请编号")
    private Long id;
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    @ApiModelProperty(value = "会员名称")
    private String buyerName;
    @ApiModelProperty(value = "提现金额")
    private BigDecimal withdrawalAmount;
    @ApiModelProperty(value = "收款账号")
    private String bankAccount;
    @ApiModelProperty(value = "收款人")
    private String bankPeople;
    @ApiModelProperty(value = "收款银行")
    private String bankName;
    @ApiModelProperty(value = "收款人手机号")
    private String bankPhone;
    @ApiModelProperty(value = "提现申请进度（1提现申请中,2申请取消,3提现申请驳回,4提现审核通过发放申请中,5发放驳回,6发放通过）")
    private Integer auditStatus;
    @ApiModelProperty(value = "申请驳回原因")
    private String auditReason;
    @ApiModelProperty(value = "提现发放id")
    private Long issueId;
    @ApiModelProperty(value = "支付时间")
    private Date payTime;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;

}
