/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 规格值表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
public class SpecValueExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "所属规格id")
    private Long specId;
    @ExcelProperty(value = "规格值名称")
    private String specValueName;
    @ExcelProperty(value = "规格图片")
    private String specValueImage;
    @ExcelProperty(value = "规格值排序")
    private Integer specValueSort;
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
