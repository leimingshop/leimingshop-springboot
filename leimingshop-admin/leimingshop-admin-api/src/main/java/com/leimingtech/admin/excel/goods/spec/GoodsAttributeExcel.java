/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.goods.spec;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品属性导出对象
 *
 * @author xuzhch x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
public class GoodsAttributeExcel {

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty(value = "属性名称")
    private String attrName;

    @ExcelProperty(value = "属性值（多个属性值之间用逗号隔开）")
    private String attrValue;

    @ExcelProperty(value = "商品ID")
    private Long goodsId;

    @ExcelProperty(value = "展示类型（默认0单选，1下拉框，2多选）")
    private Integer showType;

    @ExcelProperty(value = "是否索引（预留字段）")
    private Integer isKey;

    @ExcelProperty(value = "排序")
    private Integer sort;

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
