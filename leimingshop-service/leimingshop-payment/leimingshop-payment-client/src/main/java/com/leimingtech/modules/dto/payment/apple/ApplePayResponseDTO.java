/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.apple;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2020/11/19 16:26
 */
@Data
@ApiModel("苹果内购实体")
public class ApplePayResponseDTO {

    /**
     * 支付编号
     */
    private String paySn;


}
