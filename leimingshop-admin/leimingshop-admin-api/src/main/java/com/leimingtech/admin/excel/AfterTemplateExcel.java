/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 售后模板
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-17
 */
@Data
public class AfterTemplateExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "值")
    private String value;
    @ExcelProperty(value = "店铺ID")
    private Long storeId;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
