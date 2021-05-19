/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户管理
 *
 * @since 1.0.0
 */
@Data
public class SysUserExcel {
    @ExcelProperty(value = "用户名")
    private String username;
    @ExcelProperty(value = "姓名")
    private String realName;
    @ExcelProperty(value = "性别")
    private Integer gender;
    @ExcelProperty(value = "邮箱")
    private String email;
    @ExcelProperty(value = "手机号")
    private String mobile;
    @ExcelProperty(value = "部门名称")
    private String deptName;
    @ExcelProperty(value = "状态")
    private Integer status;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
