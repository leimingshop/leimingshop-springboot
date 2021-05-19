/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 文章表
 *
 * @author xuzhch x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
public class ArticleExcel {
    @ExcelProperty("索引id")
    private Long id;
    @ExcelProperty(value = "分类id")
    private Long acId;
    @ExcelProperty(value = "跳转链接")
    private String articleUrl;
    @ExcelProperty(value = "标题")
    private String articleTitle;
    @ExcelProperty(value = "内容")
    private String articleContent;
    @ExcelProperty(value = "是否显示（0不显示，默认为1显示）")
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
