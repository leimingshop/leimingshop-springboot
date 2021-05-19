/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.goods.record;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品状态记录表
 *
 * @author weixianchun ${email}
 * @since 1.0.0 2019-06-04
 */
@Data
public class GoodsRecordExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "商品id")
    private Long goodsId;
    @ExcelProperty(value = "商品当前状态信息10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodsState;
    @ExcelProperty(value = "下一步商品状态信息10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer changeState;
    @ExcelProperty(value = "商品状态描述")
    private String stateInfo;
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
