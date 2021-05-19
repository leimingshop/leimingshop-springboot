/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author: lishuo
 * @Date: 2020/6/17 16:11
 * @email: lishuo@leimingtech.com
 * @Description: 店铺商品分类新增导入模板
 */
@Data
public class StoreGoodsClassTemplateExcel {
    @ExcelProperty("一级店铺商品分类名称")
    private String firstStoreGoodsClassName;
    @ExcelProperty("二级店铺商品分类名称")
    private String secondStoreGoodsClassName;
    @ExcelProperty("排序")
    private Integer sort;
}