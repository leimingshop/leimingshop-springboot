/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.goods.spec;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品规格属性与属性值关联导出对象
 *
 * @author xuzhch x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
public class SpecAttributeRelationExcel {

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty(value = "商品规格ID")
    private Long specId;

    @ExcelProperty(value = "商品规格属性ID")
    private Long specAttrId;

    @ExcelProperty(value = "商品规格属性值ID")
    private Long specAttrValueId;

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
