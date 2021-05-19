/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description H5支付宝支付返回实体
 * @Author huangkeyuan
 * @Date 10:51 2019-12-10
 * @return
 */
@Data
@ApiModel(description = "AliPayH5ResponseDTO")
public class AliPayH5ResponseDTO implements Serializable {

    @ApiModelProperty("返回状态码")
    private Integer code;

    @ApiModelProperty("返回前端提示信息")
    private String msg;

    @ApiModelProperty("支付宝返回信息")
    private String orderString;

}
