/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 商品分类表
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */
@Data
public class GoodsClassExcel {
    @ExcelProperty(value = "排序")
    private Integer gcSort;


    @ExcelProperty(value = "一级分类名称")
    private String firstGcName;

    @ExcelProperty(value = "二级分类名称")
    private String secondGcName;

    @ExcelProperty(value = "三级分类名称")
    private String thirdGcName;

}