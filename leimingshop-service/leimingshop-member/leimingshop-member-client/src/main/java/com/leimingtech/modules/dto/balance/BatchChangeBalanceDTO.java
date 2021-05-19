/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.balance;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.modules.dto.member.MemberNameDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 批量变更用户余额信息
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */
@Data
@ApiModel(description = "BatchChangeBalanceDTO")
public class BatchChangeBalanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "用户信息不能为空", groups = AddGroup.class)
    private List<MemberNameDTO> memberNameDTOList;
    @ApiModelProperty(value = "余额变动（正为加，负为减）")
    @NotNull(message = "余额不能为空", groups = AddGroup.class)
    private BigDecimal changeBalance;
    @ApiModelProperty(value = "余额变动类型（0为增加或减少金额，1为调整后金额）")
    @NotNull(message = "余额变动类型不能为空", groups = AddGroup.class)
    private Integer changeType;
    @ApiModelProperty(value = "余额类型(1:运营端调整2:用户购买下单3:订单退款4:用户充值5:商家返利)")
    private Integer balanceType;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}
