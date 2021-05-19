/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 登录日志
 *
 * @since 1.0.0
 */
@Data
public class SysLogLoginExcel {
    @ExcelProperty(value = "用户操作")
    private String operation;
    @ExcelProperty(value = "状态")
    private Integer status;
    @ExcelProperty(value = "User-Agent")
    private String userAgent;
    @ExcelProperty(value = "操作IP")
    private String ip;
    @ExcelProperty(value = "用户名")
    private String creator;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
