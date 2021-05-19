/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户余额明细信息
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */
@Data
@ApiModel(description = "MemberBalanceInfoDTO")
public class MemberBalanceInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "是否设置余额支付密码(1：有密码，0：无密码)")
    private Integer payPwdFlag;
    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;
    @ApiModelProperty(value = "冻结余额")
    private BigDecimal blockedBalance;
    @ApiModelProperty(value = "可提现余额")
    private BigDecimal availableWithdrawal;

    @ApiModelProperty(value = "当天是否申请提现过(1：申请提现过，0：没有申请过)")
    private Integer applyFlag;

    @ApiModelProperty(value = "当天是否绑定过银行卡(1：绑定过，0：没有绑定过)")
    private Integer bindFlag;

}
