/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: weixianchun
 * @Description: 支付接口配置DTO(页面保存)
 * @Date :2019/5/20 14:52
 * @Version V1.0
 **/
@Data
@ToString
public class PaymentConfigUpdateDTO implements Serializable {

    /**
     * 支付索引id
     */
    @NotBlank
    @ApiModelProperty(value = "支付索引id")
    private Long id;
    /**
     * 商户号
     */
    @NotBlank(message = "商户号不可为空", groups = UpdateGroup.class)
    @ApiModelProperty(value = "商户号")
    private String paymentAccount;
    /**
     * 商户公众号App-id
     */
    @NotBlank(message = "商户公众号App-id不可为空", groups = UpdateGroup.class)
    @ApiModelProperty(value = "商户公众号App-id")
    private String appId;
    /**
     * Appsecret
     */
    @ApiModelProperty(value = "Appsecret")
    private String secretKey;
    /**
     * 商户API密钥
     */
    @ApiModelProperty(value = "商户API密钥")
    private String secretKeyApi;
    /**
     * 状态（默认0禁用，1启用）
     */
    @ApiModelProperty(value = "状态（默认0禁用，1启用）")
    private Integer paymentState;

}
