/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author huangkeyuan
 * @Description 高级设置
 * @Date 17:05 2019-05-14
 * @Param
 * @return
 */
@Data
@ApiModel(description = "SettingSeniorDTO")
public class SettingSeniorDTO implements Serializable {

    @ApiModelProperty(value = "正常订单超过：XX分 未付款，订单自动关闭")
    private String cancelOrder;

    @ApiModelProperty(value = "发货超过xx天未收货，订单自动完成")
    private String autoCompleteOrder;

    @ApiModelProperty(value = "订单完成超过：xx天自动结束交易，不能申请退货售后")
    private String cannotReturn;

    @ApiModelProperty(value = "订单完成超过xx天，不能换货")
    private String cannotBarter;

    @ApiModelProperty(value = "订单完成超过：xx天自动好评")
    private String autoComment;

}
