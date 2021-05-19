/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.wxpay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:微信退款响应实体
 * @Date: 2019/6/22 14:56
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "H5WxRefundResponseDTO")
public class H5WxRefundResponseDTO implements Serializable {

    @ApiModelProperty("返回状态码")
    private String return_code;

    @ApiModelProperty("返回信息")
    private String return_msg;

    @ApiModelProperty("业务结果")
    private String result_code;

    @ApiModelProperty("错误代码")
    private String err_code;

    @ApiModelProperty("错误代码描述")
    private String err_code_des;

    @ApiModelProperty("微信订单号")
    private String transaction_id;

    @ApiModelProperty("商户订单号")
    private String out_trade_no;

    @ApiModelProperty("商户退款单号")
    private String out_refund_no;

    @ApiModelProperty("微信退款单号")
    private String refund_id;

    @ApiModelProperty("退款金额")
    private String refund_fee;

    @ApiModelProperty("订单总金额")
    private String total_fee;

}
