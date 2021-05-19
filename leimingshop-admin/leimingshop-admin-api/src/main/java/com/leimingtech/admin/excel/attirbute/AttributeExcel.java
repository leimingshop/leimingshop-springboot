/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.attirbute;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 属性表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Data
public class AttributeExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "属性名称")
    private String attrName;
    @ExcelProperty(value = "0:text; 1:image")
    private Integer attrFormat;
    @ExcelProperty(value = "属性值列（所有属性值组合，中间用“,”隔开）")
    private String attrValue;
    @ExcelProperty(value = "排序")
    private Integer attrSort;
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
