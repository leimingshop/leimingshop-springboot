/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.aftersale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.leimingtech.modules.aftersale.converter.LongToStrFormartConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退货表
 *
 * @author huangkeyuan huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
public class AftersaleAuditExcel {

    @ExcelProperty(value = "申请单号", converter = LongToStrFormartConverter.class)
    private Long aftersaleSn;
    @ExcelProperty(value = "商品货号")
    private String specSerial;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "会员名称")
    private String memberName;
    @ExcelProperty(value = "申请时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "审核时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date auditDate;
    @ExcelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    /**
     * "审核状态",replace = {"审核通过_1","审核拒绝_2","待审核_3","用户取消_4"
     */
    @ExcelProperty(value = "审核状态")
    private Integer result;
    /**
     * "申请类型",replace = {"退货_0","换货_1","仅退款_2"
     */
    @ExcelProperty(value = "申请类型")
    private Integer aftersaleType;

}
