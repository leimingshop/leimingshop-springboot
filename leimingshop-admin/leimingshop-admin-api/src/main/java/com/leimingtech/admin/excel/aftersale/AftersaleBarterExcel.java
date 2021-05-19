/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.aftersale;

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
    @ExcelProperty(value = "商户名称")
    private String storeName;
    @ExcelProperty(value = "商品货号")
    private String specSerial;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "用户名")
    private String memberName;
    @ExcelProperty(value = "申请时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * "换货状态",replace = {"用户取消_1","退款失败_2","待退货入库_3","确认收货_4","退款中_5","退款成功_6","换货失败_7","待换货入库_8","换货出库中_9","换货成功_10"
     */
    @ExcelProperty(value = "换货状态", converter = AftersaleStatusConverter.class)
    private Integer aftersaleStatus;
}
