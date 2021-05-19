/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.evaluate;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 信誉评价表
 *
 * @author chengqian
 * @since 7.0 2019-05-15
 */
@Data
public class EvaluateGoodsExcel {

    @ExcelProperty(value = "商品信息")
    private String goodsName;
    @ExcelProperty(value = "评价内容")
    private String evaluateContent;
    @ExcelProperty(value = "用户信息")
    private String memberName;
    @ExcelProperty(value = "评价时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "店铺名称")
    private String storeName;
    @ExcelProperty(value = "回复内容")
    private String replyContent;
    @ExcelProperty(value = "状态")
    private Integer evaluateState;


}
