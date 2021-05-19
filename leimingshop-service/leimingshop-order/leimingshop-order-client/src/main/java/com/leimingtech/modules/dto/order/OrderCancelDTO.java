/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderCancelDTO
 * @Description 取消订单
 * @Author DY
 * @Date 2019/6/18 16:28
 * @Version 1.0
 **/
@Data
@ApiModel(description = "OrderCancelDTO")
public class OrderCancelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "订单取消原因Id")
    private Long reasonId;
}
