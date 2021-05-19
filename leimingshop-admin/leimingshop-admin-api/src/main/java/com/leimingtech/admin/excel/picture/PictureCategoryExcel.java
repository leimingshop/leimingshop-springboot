/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.picture;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 图片分类表
 *
 * @since 1.0 2019-05-10
 */
@Data
public class PictureCategoryExcel {
    @ApiModelProperty(value = "主键")
    @ExcelProperty("主键")
    private Long id;
    @ApiModelProperty(value = "图片分类名称")
    @ExcelProperty("图片分类名称")
    private String categoryName;
    @ApiModelProperty(value = "店铺ID")
    @ExcelProperty("店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "创建人")
    @ExcelProperty("创建人")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ApiModelProperty(value = "更新人")
    @ExcelProperty(value = "更新人")
    private String updater;
    @ApiModelProperty(value = "更新时间")
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
