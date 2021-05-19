/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
@ColumnWidth(25)
public class ImportErrorExcel {

    @ColumnWidth(50)
    @ExcelProperty(value = "失败id")
    private String failureId;

    @ColumnWidth(50)
    @ExcelProperty(value = "失败原因")
    private String failureReasons;

}
