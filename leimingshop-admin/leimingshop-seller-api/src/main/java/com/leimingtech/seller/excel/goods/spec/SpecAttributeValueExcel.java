/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.goods.spec;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品规格属性值导出对象
 *
 * @author xuzhch x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
public class SpecAttributeValueExcel {

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty(value = "商品ID")
    private Long goodsId;

    @ExcelProperty(value = "商品规格属性ID")
    private Long specAttrId;

    @ExcelProperty(value = "商品规格属性值")
    private String specAttrValue;

    @ExcelProperty(value = "图片")
    private String specPicture;
    @ExcelProperty(value = "是否选中（默认0未选中，1选中）")
    private Integer isSelect;

    @ExcelProperty(value = "是否是主规格属性值（默认0否，1是）")
    private Integer isMain;

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
