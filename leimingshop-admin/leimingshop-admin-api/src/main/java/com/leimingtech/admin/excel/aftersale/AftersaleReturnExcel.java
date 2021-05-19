/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.aftersale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.leimingtech.modules.aftersale.converter.AftersaleStatusConverter;
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
public class AftersaleReturnExcel {

    @ExcelProperty(value = "退货单号", converter = LongToStrFormartConverter.class)
    private Long aftersaleSn;
    @ExcelProperty(value = "商品货号")
    private String specSerial;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "会员名称")
    private String memberName;
    @ExcelProperty(value = "商户名称")
    private String storeName;
    @ExcelProperty(value = "申请时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    /**
     * "退货状态",replace = {"用户取消_1","退款失败_2","待退货入库_3","退款中_4","退款成功_5","换货失败_6","待换货入库_7","换货出库中_8","换货成功_9"
     */
    @ExcelProperty(value = "退货状态", converter = AftersaleStatusConverter.class)
    private Integer aftersaleStatus;
}
