/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 可支持售后方式
 * @Date: 2019/6/17 18:38
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AftersaleSettingDTO")
public class AftersaleSettingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品退货(1:开启，0:关闭)")
    private String goodsReturn;

    @ApiModelProperty(value = "商品换货(1:开启，0:关闭)")
    private String goodsBarter;

    @ApiModelProperty(value = "订单完成时间")
    private Date completeTime;

    @ApiModelProperty(value = "订单完成超过：xx天自动结束交易，不能申请退货售后")
    private String cannotReturn;

    @ApiModelProperty(value = "订单完成超过xx天，不能换货")
    private String cannotBarter;

}
