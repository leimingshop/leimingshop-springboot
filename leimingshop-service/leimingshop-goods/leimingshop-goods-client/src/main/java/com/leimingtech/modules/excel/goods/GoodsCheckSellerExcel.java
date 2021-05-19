/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品审核导出实体
 *
 * @author dy 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
public class GoodsCheckSellerExcel {

    @ExcelProperty(value = "商品货号")
    private Long goodsSerial;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "品牌名称")
    private String brandName;
    @ExcelProperty(value = "商品分类名称")
    private String gcName;
    @ExcelProperty(value = "销售价")
    private BigDecimal specSellPrice;
    @ExcelProperty(value = "成本价")
    private BigDecimal specCostPrice;
    @ExcelProperty(value = "审核状态")
    private Integer goodsStatus;


}
