/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 规格分组表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
public class SpecGroupExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "规格分组名称")
    private String groupName;
    @ExcelProperty(value = "规格数量")
    private String lableNum;
    @ExcelProperty(value = "规格分组排序")
    private String sort;
    @ExcelProperty(value = "规格分组状态（默认1:启用,2:禁用）")
    private String groupStatus;
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
