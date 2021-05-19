/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.aftersale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.leimingtech.modules.aftersale.converter.AftersaleStatusConverter;
import com.leimingtech.modules.aftersale.converter.LongToStrFormartConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单换货导出实体
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Slf4j
@Data
public class AftersaleBarterExcel implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "换货单号", converter = LongToStrFormartConverter.class)
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
    @ExcelProperty(value = "换货状态", converter = AftersaleStatusConverter.class)
    private Integer aftersaleStatus;
}
