/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.aftersale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.leimingtech.modules.aftersale.converter.ArbitrationTypeConverter;
import com.leimingtech.modules.aftersale.converter.AuditResultConverter;
import com.leimingtech.modules.aftersale.converter.LongToStrFormartConverter;
import lombok.Data;

import java.util.Date;

/**
 * 仲裁申请
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:45
 **/
@Data
public class ArbitrationExcel {

    @ExcelProperty(value = "申请单号", converter = LongToStrFormartConverter.class)
    private Long aftersaleSn;
    @ExcelProperty(value = "会员账号")
    private String memberName;
    @ExcelProperty(value = "商品货号")
    private String specSerial;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "商品名称")
    private Date arbitrationApplyDate;
    @ExcelProperty(value = "仲裁审核时间")
    private Date arbitrationAuditDate;
    @ExcelProperty(value = "仲裁状态", converter = AuditResultConverter.class)
    private Integer arbitrationStatus;
    @ExcelProperty(value = "仲裁类型", converter = ArbitrationTypeConverter.class)
    private Integer arbitrationType;
}
