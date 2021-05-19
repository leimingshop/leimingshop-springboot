/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.goods.time;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 定时上架商品表
 *
 * @author weixianchun ${email}
 * @since 1.0.0 2019-06-04
 */
@Data
public class GoodsTimeExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "商品ID")
    private Long goodsId;
    @ExcelProperty(value = "店铺ID")
    private Long storeId;
    @ExcelProperty(value = "上架时间")
    private Date shelfTime;
    @ExcelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodsState;
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
