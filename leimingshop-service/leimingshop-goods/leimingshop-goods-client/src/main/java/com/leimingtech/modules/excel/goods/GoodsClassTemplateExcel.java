/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: taotie
 * @Date: 2020/6/12 11:58
 * @email: lishuo@leimingtech.com
 * @Description: 平台分类的模板导出
 */
@Data
public class GoodsClassTemplateExcel implements Serializable {
    @ExcelProperty("排序")
    private Integer gcSort;

    @ExcelProperty("一级分类名称_CN")
    private String firstGcName;

    @ExcelProperty("二级分类名称_CN")
    private String secondGcName;

    @ExcelProperty("三级分类名称_CN")
    private String thirdGcName;

    @ExcelProperty("分类类型")
    private Integer classType;
}