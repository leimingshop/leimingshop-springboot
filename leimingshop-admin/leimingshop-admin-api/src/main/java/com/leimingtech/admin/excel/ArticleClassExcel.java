/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 文章分类表
 *
 * @author xuzhch x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
public class ArticleClassExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "分类标识码")
    private String acCode;
    @ExcelProperty(value = "分类名称")
    private String acName;
    @ExcelProperty(value = "父ID")
    private Long acParentId;
    @ExcelProperty(value = "分类状态（0停用，默认为1启用）")
    private Integer status;
    @ExcelProperty(value = "排序")
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
