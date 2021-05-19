/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.apple;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2020/11/19 16:26
 */
@Data
@ApiModel("苹果内购实体")
public class ApplePayDTO {

    private Long paySn;

    @ApiModelProperty("票据")
    private String receipt;


}
