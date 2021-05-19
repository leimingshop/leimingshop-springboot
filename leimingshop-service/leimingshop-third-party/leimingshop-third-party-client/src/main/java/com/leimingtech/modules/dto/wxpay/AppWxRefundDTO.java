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
 * @Description:微信退款实体
 * @Date: 2019/6/22 14:56
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "AppWxRefundDTO")
public class AppWxRefundDTO implements Serializable {

    @ApiModelProperty("退款单号，也叫微信支付单号，每次必须退款的时候每次必须不一致")
    private String outrefundno;

    @ApiModelProperty("支付单号")
    private String outtradeno;

    @ApiModelProperty("订单号")
    private String ordersn;

    @ApiModelProperty("总金额(分)")
    private Integer totalfee;

    @ApiModelProperty("退款金额(分)")
    private Integer refundfee;

    @ApiModelProperty("随机字符串")
    private Long nonceStr;

    @ApiModelProperty("微信公众号appid")
    private String appId;

    @ApiModelProperty("微信公众号appSecret")
    private String appSecret;

    @ApiModelProperty("微信商户mch_id")
    private String mchId;

    @ApiModelProperty("微信商户api_key")
    private String apikey;


}
