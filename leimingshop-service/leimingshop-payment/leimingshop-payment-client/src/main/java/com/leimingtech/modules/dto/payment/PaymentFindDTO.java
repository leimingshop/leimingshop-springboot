/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author: weixianchun
 * @Description: 支付方式DTO
 * @Date :2019/5/20 14:53
 * @Version V1.0
 **/
@Data
@ApiModel(description = "PaymentFindDTO")
public class PaymentFindDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 支付方式表主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 支付对接配置信息（json格式）
     */
    @ApiModelProperty(value = "支付对接配置信息（json格式）")
    private String paymentConfig;
    /**
     * 状态（默认0禁用，1启用）
     */
    @ApiModelProperty(value = "状态（默认0禁用，1启用）")
    private Integer paymentState;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;

}
