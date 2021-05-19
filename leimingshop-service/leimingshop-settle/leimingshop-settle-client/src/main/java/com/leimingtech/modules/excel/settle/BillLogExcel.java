/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.settle;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 对账操作记录
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
public class BillLogExcel {

    @ExcelProperty(value = "创建时间")
    private Date createDate;
    @ExcelProperty(value = "店铺名称")
    private String storeName;
    @ExcelProperty(value = "操作人")
    private String operator;
    @ExcelProperty(value = "操作类型")
    private String operatorType;

}
