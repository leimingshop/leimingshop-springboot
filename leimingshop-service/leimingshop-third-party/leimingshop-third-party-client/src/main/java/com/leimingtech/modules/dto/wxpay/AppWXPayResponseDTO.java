/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.wxpay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangtai
 * @date 2020/4/7 14:15
 * @Description:
 */
@Data
@ApiModel("AppWXPayResponseDTO")
public class AppWXPayResponseDTO {

    @ApiModelProperty("返回状态码")
    private Integer code;

    @ApiModelProperty("返回前端提示信息")
    private String msg;

    @ApiModelProperty("微信公众账号ID")
    private String appId;

    @ApiModelProperty("商户号")
    private String partnerId;

    @ApiModelProperty("预支付交易会话ID")
    private String prepayId;

    @ApiModelProperty("随机字符串，不长于32位")
    private String nonceStr;

    @ApiModelProperty("时间戳")
    private String timeStamp;

    @ApiModelProperty("签名")
    private String sign;

    @ApiModelProperty("暂填写固定值Sign=WXPay")
    private String packageValue;

}
