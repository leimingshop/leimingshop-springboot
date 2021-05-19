/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.updatelog;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 版本更新日志
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
public class UpdateLogExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "更新日志标题")
    private String logTitle;
    @ExcelProperty(value = "版本号")
    private String logVersion;
    @ExcelProperty(value = "更新日志类型（0优化迭代；1新增功能）")
    private Integer logType;
    @ExcelProperty(value = "日志更新内容")
    private String logDec;
    @ExcelProperty(value = "创建者")
    private String creator;
    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "更新者")
    private String updater;
    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}