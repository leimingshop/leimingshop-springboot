/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.wxpay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 微信签名实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/3/17 16:56
 **/
@Data
@ToString
@ApiModel(description = "WxSignatureDTO")
public class WxSignatureDTO {

    @ApiModelProperty("生成签名的时间戳（密文）")
    private String timestamp;

    @ApiModelProperty("生成签名的随机串（密文）")
    private String nonceStr;

    @ApiModelProperty("签名（密文）")
    private String signature;

}
