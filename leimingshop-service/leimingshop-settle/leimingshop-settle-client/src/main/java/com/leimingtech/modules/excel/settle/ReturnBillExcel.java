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
 * 退货结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
public class ReturnBillExcel {

    @ExcelProperty(value = "退货编号")
    private Long returnSn;
    @ExcelProperty(value = "申请时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;
    @ExcelProperty(value = "商品货号")
    private String specSerial;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "退款金额")
    private BigDecimal returnAmount;
    @ExcelProperty(value = "支付方式")
    private String paymentName;
    @ExcelProperty(value = "平台处理时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date manageTime;

}
