/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.wechat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:支付回调实体
 * @Date: 2019/6/13 16:17
 * @Version: V1.0
 */
@Data
@ApiModel(description = "H5WXPayNotifyDTO")
public class H5WXPayNotifyDTO implements Serializable {

    @ApiModelProperty("回调返回码")
    private String resultCode;

    @ApiModelProperty("回调返回结果")
    private String result;

    @ApiModelProperty("支付金额")
    private String totalFee;

    @ApiModelProperty("交易单号")
    private String paySn;

    @ApiModelProperty("微信支付交易单号")
    private String transactionId;

    @ApiModelProperty("支付方式id")
    private String paymentId;

}
