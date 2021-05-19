/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.goods;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: lishuo
 * @Date: 23/6/2020 09:09
 * @email: lishuo@leimingtech.com
 * @Description: 商品导入的模板
 */
@Data
public class GoodsTemplateExcel implements Serializable {
    @ColumnWidth(25)
    @ExcelProperty("商品分类名称")
    private String goodsGcName;
    @ColumnWidth(25)
    @ExcelProperty("店铺分类名称")
    private String storeGcName;
    @ColumnWidth(25)
    @ExcelProperty("商品名称")
    private String goodsName;
    @ColumnWidth(25)
    @ExcelProperty("副标题")
    private String goodsSubName;
    @ColumnWidth(10)
    @ExcelProperty("标签")
    @ContentStyle(verticalAlignment = VerticalAlignment.CENTER)
    private String goodsLabel;
    @ColumnWidth(10)
    @ExcelProperty("商品售价")
    private BigDecimal goodsSellPrice;
    @ExcelProperty("商品的成本价")
    @ColumnWidth(10)
    private BigDecimal goodsCostPrice;
    @ExcelProperty("品牌")
    @ColumnWidth(10)
    private String goodsBrand;
    /**
     * ("配送方式 （1快递 2自提 3无需物流 4电子提货码）")
     */
    @ExcelIgnore
    private Integer deliveryType;
    @ColumnWidth(15)
    @ExcelProperty("配送方式 （0无需物流 1快递 2自提 3电子提货码）")
    private Integer expressFlag;
    @ExcelProperty("运费模板名称")
    @ColumnWidth(15)
    private String freightTemplate;
    @ColumnWidth(15)
    @ExcelProperty("运费承担方式(0 无需物流 1 买家承担 2 卖家承担)")
    private Integer freightBearType;
    @ExcelProperty("发票（0：无，1：有）")
    @ColumnWidth(10)
    private Integer invoice;
    @ColumnWidth(15)
    @ExcelProperty("发票类型(多选 1:电子、2：纸质、3：增值税普通发票)")
    private String invoiceType;
    @ColumnWidth(15)
    @ExcelProperty("发票内容（多选 1：商品明细、2：商品类别）")
    private String invoiceContent;
    @ExcelProperty("SPU属性")
    @ColumnWidth(30)
    private String goodsAttrAndValue;
    @ExcelProperty("SKU规格名称和值")
    @ColumnWidth(40)
    private String goodsSepcNameValue;
    @ColumnWidth(30)
    @ExcelProperty("商品设置（售价，成本价，库存）")
    private String goodsSetting;
    @ColumnWidth(30)
    @ExcelProperty("售后说明模板内容")
    private String afterSaleTemplate;
    @ColumnWidth(30)
    @ExcelProperty("商品主图地址")
    private String pictureUrl;
    @ColumnWidth(40)
    @ExcelProperty("商品sku图片地址")
    private String skuPictureUrl;
    @ColumnWidth(40)
    @ExcelProperty("商品详情")
    private String goodsInfo;
    @ExcelIgnore()
    private Integer totalPage;
}
