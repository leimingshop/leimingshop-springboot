/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.wechat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 小程序支付返回实体
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/19 19:52
 **/
@Data
@ApiModel(description = "MiniPayResponseDTO")
public class MiniPayResponseDTO implements Serializable {

    private static final long serialVersionUID = -2960655312288167188L;

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
}
