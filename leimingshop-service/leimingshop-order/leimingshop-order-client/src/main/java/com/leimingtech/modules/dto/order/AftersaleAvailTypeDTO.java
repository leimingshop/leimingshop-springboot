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
@ApiModel(description = "AftersaleAvailTypeDTO")
public class AftersaleAvailTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单完成时间")
    private Date completeTime;

    @ApiModelProperty(value = "是否可申请退货(0:否，1:是)")
    private Integer isReturn;

    @ApiModelProperty(value = "不可申请退货原因")
    private String notReturnReson;

    @ApiModelProperty(value = "是否可申请换货(0:否，1:是)")
    private Integer isBarter;

    @ApiModelProperty(value = "不可申请换货原因")
    private String notBarterReson;

}
