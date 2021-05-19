/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 参数管理
 *
 * @since 1.0.0
 */
@Data
public class SysParamsExcel {
    @ExcelProperty(value = "参数编码")
    private String paramCode;
    @ExcelProperty(value = "参数值")
    private String paramValue;
    @ExcelProperty(value = "备注")
    private String remark;

}