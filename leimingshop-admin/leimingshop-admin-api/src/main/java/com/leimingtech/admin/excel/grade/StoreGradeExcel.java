/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.grade;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.leimingtech.modules.aftersale.converter.LongToStrFormartConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺等级表
 *
 * @author xuzhch x170518@163.com
 * @since 1.0.0 2019-05-30
 */
@Data
public class StoreGradeExcel {
    @ExcelProperty(value = "ID", converter = LongToStrFormartConverter.class)
    private Long id;
    @ExcelProperty(value = "等级名称")
    private String sgName;
    @ExcelProperty(value = "等级分值")
    private Integer sgGradeScore;
    @ExcelProperty(value = "允许发布的商品数量(默认为0)")
    private Integer sgGoodsLimit;
    @ExcelProperty(value = "收费标准")
    private BigDecimal sgPrice;
    @ExcelProperty(value = "佣金比例")
    private Float brokerageScale;
    @ExcelProperty(value = "是否启用（0禁用，默认为1启用）")
    private Integer showFlag;
    @ExcelProperty(value = "排序字段")
    private Integer sort;
    @ExcelProperty(value = "创建者")
    private String creator;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "更新者")
    private String updater;
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
