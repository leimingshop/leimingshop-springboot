/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 异常日志
 *
 * @since 1.0.0
 */
@Data
public class SysLogErrorExcel {
    @ExcelProperty(value = "模块名称")
    private String module;
    @ExcelProperty(value = "请求URI")
    private String requestUri;
    @ExcelProperty(value = "请求方式")
    private String requestMethod;
    @ExcelProperty(value = "请求参数")
    private String requestParams;
    @ExcelProperty(value = "User-Agent")
    private String userAgent;
    @ExcelProperty(value = "操作IP")
    private String ip;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
