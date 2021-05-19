/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.goods.spec;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品规格表
 *
 * @author weixianchun ${email}
 * @since 1.0.0 2019-06-04
 */
@Data
public class GoodsSpecExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "商品ID")
    private Long goodsId;
    @ExcelProperty(value = "商品规格编号")
    private String specSerial;
    @ExcelProperty(value = "商品规格名称")
    private String specName;
    @ExcelProperty(value = "规格库存")
    private Integer specStorage;
    @ExcelProperty(value = "销售价")
    private BigDecimal specSellPrice;
    @ExcelProperty(value = "成本价")
    private BigDecimal specCostPrice;
    @ExcelProperty(value = "商品规格属性值名称（中间用逗号隔开）")
    private String specAttrName;
    @ExcelProperty(value = "是否启用（默认0启用，1未启用）")
    private Integer isEnable;
    @ExcelProperty(value = "商品规格主图")
    private String specMainPicture;
    @ExcelProperty(value = "规格销售数量")
    private Integer specSaleNum;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
