/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.wechat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @param
 * @Description 查询微信支付结果返回信息
 * @Author huangkeyuan
 * @Date 20:05 2019-12-05
 * @return
 */
@Data
@ApiModel(description = "H5WXQueryOrderResponseDTO")
public class H5WXQueryOrderResponseDTO implements Serializable {

    @ApiModelProperty("支付结果码")
    private String resultCode;

    @ApiModelProperty("支付结果描述")
    private String resultMsg;


}
