/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.stock;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 库存修改记录表
 *
 * @author chengqian 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Data
public class StockLogExcel {

    @ExcelProperty(value = "sku")
    private Long sku;
    @ExcelProperty(value = "商品名称")
    private String goodsName;
    @ExcelProperty(value = "规格名称")
    private String specName;
    @ExcelProperty(value = "品牌名称")
    private String brandName;
    @ExcelProperty(value = "修改前库存")
    private Integer beforeRepertory;
    @ExcelProperty(value = "修改后库存")
    private Integer afterRepertory;
    @ExcelProperty(value = "修改人")
    private String updater;
    @ExcelProperty(value = "修改时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
