/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 提现日志
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Data
@ApiModel(value = "提现日志")
public class WithdrawalApplicationLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "申请id")
    private Long applyId;
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    @ApiModelProperty(value = "提现申请进度（1提现申请中,2申请取消,3提现申请驳回,4提现审核通过发放申请中,5发放驳回,6发放通过）")
    private Integer auditStatus;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;
}
