/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 规格表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
public class SpecExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "规格名称")
    private String specName;
    @ExcelProperty(value = "0:text; 1:image")
    private Integer specFormat;
    @ExcelProperty(value = "规格值列（所有规格值组合，中间用“,”隔开）")
    private String specValue;
    @ExcelProperty(value = "排序")
    private Integer spSort;
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
