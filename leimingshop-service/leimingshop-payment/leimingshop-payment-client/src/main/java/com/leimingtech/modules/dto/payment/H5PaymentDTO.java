/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author: 刘远杰
 * @Description: h5支付列表支付方式实体
 * @Date :2019/8/17
 * @Version V1.0
 **/
@Data
@ApiModel(description = "H5PaymentDTO")
public class H5PaymentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 支付方式表主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 支付标识
     */
    @ApiModelProperty(value = "支付标识")
    private String paymentCode;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String paymentName;

    /**
     * 支付图标
     */
    @ApiModelProperty(value = "支付图标")
    private String paymentLogo;

}
