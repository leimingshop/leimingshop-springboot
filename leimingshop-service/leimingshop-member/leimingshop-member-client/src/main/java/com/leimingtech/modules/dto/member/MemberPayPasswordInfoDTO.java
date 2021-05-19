/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 用户支付信息
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-07-20 15:20
 **/
@Data
@ApiModel(description = "MemberPayPasswordInfoDTO")
public class MemberPayPasswordInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户余额信息")
    private MemberBalanceInfoDTO memberBalanceInfoDTO;

    /**
     * 地区内容
     */
    @ApiModelProperty(value = "校验的支付密码是否正确")
    private String payPasswordResult;
}
