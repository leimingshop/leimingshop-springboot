/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.leimingtech.modules.excel.goods.converter.ConverterGoodsStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能描述：
 * 商品导出
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/10
 **/

@Data
@HeadRowHeight(20)
@ColumnWidth(25)
public class EasyGoodsExcel {

    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "商品货号")
    private String goodsSerial;
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
    @ExcelProperty(value = "商品上下架状态", converter = ConverterGoodsStatus.class)
    private Integer goodsShow;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;


}
