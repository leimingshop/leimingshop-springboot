/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 系统文章表
 *
 * @author xuzhch x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
public class DocumentExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "文章分类ID")
    private Long acId;
    @ExcelProperty(value = "标识码")
    private String docCode;
    @ExcelProperty(value = "标题")
    private String docTitle;
    @ExcelProperty(value = "内容")
    private String docContent;
    @ExcelProperty(value = "排序字段")
    private Integer sort;
    @ExcelProperty(value = "创建者")
    private String creator;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "更新者")
    private String updater;
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
