/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 用户银行账户信息
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-21
 */
@Data
@ApiModel(value = "用户银行账户信息")
public class MemberBankAccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    @ApiModelProperty(value = "收款账号")
    @NotBlank(message = "收款账号不能为空")
    private String bankAccount;
    @ApiModelProperty(value = "收款人")
    @NotBlank(message = "收款人不能为空")
    private String bankPeople;
    @ApiModelProperty(value = "收款银行")
    @NotBlank(message = "收款银行不能为空")
    private String bankName;
    @ApiModelProperty(value = "收款人手机号")
    @NotBlank(message = "收款人手机不能为空")
    private String bankPhone;
}
