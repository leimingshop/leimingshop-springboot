/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.settle;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
public class OrderBillExcel {

    @ExcelProperty(value = "结算单编号")
    private Long billTotalId;
    @ExcelProperty(value = "订单编号")
    private Long orderSn;
    @ExcelProperty(value = "交易时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    @ExcelProperty(value = "订单金额")
    private BigDecimal orderTotals;
    @ExcelProperty(value = "平台分佣")
    private BigDecimal commisTotals;
    @ExcelProperty(value = "店铺活动费用")
    private BigDecimal storeCostTotals;
    @ExcelProperty(value = "积分抵现")
    private BigDecimal pointAmount;
    @ExcelProperty(value = "支付方式")
    private String paymentName;

}
