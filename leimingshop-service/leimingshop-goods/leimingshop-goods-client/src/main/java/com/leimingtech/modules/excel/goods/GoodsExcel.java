/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表
 *
 * @author dy 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
public class GoodsExcel {

    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "商品货号")
    private Long goodsSerial;
    @ExcelProperty(value = "商品分类名称")
    private String gcName;
    @ExcelProperty(value = "品牌名称")
    private String brandName;
    @ExcelProperty(value = "店铺名称")
    private String storeName;
    @ExcelProperty(value = "销售价")
    private BigDecimal specSellPrice;
    @ExcelProperty(value = "成本价")
    private BigDecimal specCostPrice;
    @ExcelProperty(value = "商品上下架状态")
    private Integer goodsShow;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;


}
