/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.settle;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账汇总表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
public class BillTotalExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "对账汇总单编号")
    private String billTotalSn;
    @ExcelProperty(value = "店铺ID")
    private Long storeId;
    @ExcelProperty(value = "店铺名")
    private String storeName;
    @ExcelProperty(value = "本期应结金额")
    private BigDecimal resultTotals;
    @ExcelProperty(value = "店铺类型(1 自营2 普通)")
    private Integer storeType;
    @ExcelProperty(value = "商家确认状态（0 未确认 1 已确认）")
    private Integer confirmStatus;
    @ExcelProperty(value = "结算状态 0未结算1已结算")
    private Integer billState;
    @ExcelProperty(value = "开始日期")
    private Date obtStartTime;
    @ExcelProperty(value = "结束日期")
    private Date obtEndTime;
    @ExcelProperty(value = "出账日期")
    private Date billTime;
    @ExcelProperty(value = "结算备注")
    private String billRemark;
    @ExcelProperty(value = "商家备注")
    private String storeRemark;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}