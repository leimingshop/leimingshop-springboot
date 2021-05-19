/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 支付流水管理(导出用)
 * @Date :2019/6/18 11:26
 * @Version V1.0
 **/
@Data
@ApiModel(description = "PaymentTallyExcelDTO")
public class PaymentTallyExcelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "交易流水号")
    @ApiModelProperty(value = "交易流水号")
    private String tradeSn;

    @ExcelProperty(value = "订单编号")
    @ApiModelProperty(value = "订单编号（支付记录为父订单编号，退款记录为子订单编号）")
    private Long orderSn;

    @ExcelProperty(value = "用户名")
    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ExcelProperty(value = "交易单号")
    @ApiModelProperty(value = "交易编号")
    private Long paymentSn;

    @ExcelProperty(value = "交易时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ExcelProperty(value = "交易渠道")
    @ApiModelProperty(value = "交易渠道")
    private String paymentName;

    @ExcelProperty(value = "交易金额")
    @ApiModelProperty(value = "交易金额")
    private BigDecimal paymentAmount;

    @ExcelProperty(value = "收支标识")
    @ApiModelProperty(value = "收支标识（0:收入，1:支出）")
    private String transactionStatus;

}
