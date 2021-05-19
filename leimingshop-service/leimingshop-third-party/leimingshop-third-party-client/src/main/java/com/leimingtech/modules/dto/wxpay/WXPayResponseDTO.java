/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.wxpay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述：
 * <微信支付生成预支付订单返回实体>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/5/24 16:01
 **/
@Data
@ApiModel(description = "WXPayResponseDTO")
public class WXPayResponseDTO implements Serializable {

    @ApiModelProperty("返回状态码")
    private Integer code;

    @ApiModelProperty("返回前端提示信息")
    private String msg;

    @ApiModelProperty("微信公众账号ID")
    private String appId;

    @ApiModelProperty("时间戳")
    private String timeStamp;

    @ApiModelProperty("随机字符串")
    private String nonceStr;

    @ApiModelProperty("统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*")
    private String packages;

    @ApiModelProperty("签名算法 MD5/HMACSHA256")
    private String signType;

    @ApiModelProperty("签名")
    private String paySign;

    @ApiModelProperty("支付跳转链接")
    private String mwebUrl;

    @ApiModelProperty("trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。")
    private String codeUrl;

    @ApiModelProperty("交易类型")
    private String tradeType;


}
