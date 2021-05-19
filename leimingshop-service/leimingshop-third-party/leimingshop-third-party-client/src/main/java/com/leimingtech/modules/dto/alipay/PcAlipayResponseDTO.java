/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @Description 支付宝支付响应实体
 * @Author huangkeyuan
 * @Date 10:15 2019-12-10
 * @return
 */
@Data
@ToString
@ApiModel(description = "H5AlipayResponseDTO")
public class PcAlipayResponseDTO implements Serializable {

    @ApiModelProperty("返回状态码")
    private String return_code;

    @ApiModelProperty("返回信息")
    private String return_msg;

    @ApiModelProperty("业务结果")
    private String orderString;

}
