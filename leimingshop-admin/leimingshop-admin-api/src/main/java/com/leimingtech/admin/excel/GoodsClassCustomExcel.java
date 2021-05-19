/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品自定义分类表
 *
 * @author xuzhch 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@Data
public class GoodsClassCustomExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "父ID")
    private Long gcParentId;
    @ExcelProperty(value = "展示类目名称")
    private String gcName;
    @ExcelProperty(value = "展示类目图片")
    private String gcPic;
    @ExcelProperty(value = "展示类目url")
    private String gcUrl;
    @ExcelProperty(value = "关联商品分类id")
    private Long classId;
    @ExcelProperty(value = "层级path。格式：1,2,3")
    private String idPath;
    @ExcelProperty(value = "前台展示（0不展示，默认为1展示）")
    private Integer showFlag;
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
