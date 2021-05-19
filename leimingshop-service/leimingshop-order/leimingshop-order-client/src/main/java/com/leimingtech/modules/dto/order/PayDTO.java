/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能描述：
 * <订单保存完成返回参数到支付页面>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/20 17:36
 **/
@ApiModel(description = "PayDTO")
@Data
public class PayDTO {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("是否拆单（默认0:否,1:是）")
    private Integer isSplit;

    @ApiModelProperty("支付单号")
    private Long paySn;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("支付金额")
    private BigDecimal orderAmount;


    @ApiModelProperty("订单自动取消时间")
    private Date cancelDate;

}
