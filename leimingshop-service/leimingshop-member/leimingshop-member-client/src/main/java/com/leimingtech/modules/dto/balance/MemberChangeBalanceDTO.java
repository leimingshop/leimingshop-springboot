/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.balance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户余额明细表
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */
@Data
@ApiModel(value = "用户余额明细表")
public class MemberChangeBalanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "当前余额")
    private BigDecimal currentBalance;
    @ApiModelProperty(value = "变动前余额")
    private BigDecimal beforeBalance;
    @ApiModelProperty(value = "余额变动（正为加，负为减）")
    private BigDecimal changeBalance;
    @ApiModelProperty(value = "余额类型(1:运营端调整2:用户购买下单3:订单退款4:用户充值5:商家返利)")
    private Integer balanceType;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}