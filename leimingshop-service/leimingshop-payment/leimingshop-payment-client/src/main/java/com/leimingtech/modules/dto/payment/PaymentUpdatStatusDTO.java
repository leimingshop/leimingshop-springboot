/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * @Author: weixianchun
 * @Description: 修改开启关闭状态
 * @Date :2019/5/20 14:53
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "PaymentUpdatStatusDTO")
public class PaymentUpdatStatusDTO implements Serializable {

    /**
     * 支付索引id
     */
    private Long id;

    /**
     * 状态（默认0禁用，1启用）
     */
    @ApiModelProperty(value = "状态（默认0禁用，1启用）")
    private Integer paymentState;

}
