/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 规格与规格分组关联表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
public class SpecGroupRelationExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "规格分组ID")
    private Long groupId;
    @ExcelProperty(value = "规格ID")
    private Long specId;
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
