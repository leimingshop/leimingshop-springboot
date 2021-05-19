/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.leimingtech.modules.dto.converter.ConverterOrderStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单导出实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@ApiModel(description = "EasyOrderExcelDTO")
@HeadRowHeight(20)
@ColumnWidth(25)
public class EasyOrderExcelDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ExcelProperty(value = "订单编号")
    private String orderSn;


    @ExcelProperty(value = "商户名称")
    private String storeName;


    @ExcelProperty(value = "会员名称")
    private String buyerName;


    @ExcelProperty(value = "下单时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ExcelProperty(value = "交易时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @ExcelProperty(value = "支付方式名称")
    private String paymentName;

    @ExcelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    @ExcelProperty(value = "订单状态", converter = ConverterOrderStatus.class)
    private Integer orderStatus;


}
