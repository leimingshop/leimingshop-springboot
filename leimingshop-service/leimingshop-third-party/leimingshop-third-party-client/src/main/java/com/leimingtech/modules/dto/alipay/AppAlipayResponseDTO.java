/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangtai
 * @date 2020/4/3 15:43
 * @Description:
 */
@Data
@ToString
@ApiModel(description = "AppAlipayResponseDTO")
public class AppAlipayResponseDTO {

    @ApiModelProperty("返回状态码")
    private String return_code;

    @ApiModelProperty("返回信息")
    private String return_msg;

    @ApiModelProperty("业务结果")
    private String orderString;
}
