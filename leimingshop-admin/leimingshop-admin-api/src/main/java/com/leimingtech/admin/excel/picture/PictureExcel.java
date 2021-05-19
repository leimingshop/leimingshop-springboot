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
 * 图片表
 *
 * @since 1.0 2019-05-10
 */
@Data
public class PictureExcel {
    @ApiModelProperty(value = "主键")
    @ExcelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    @ExcelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "图片分类ID")
    @ExcelProperty(value = "图片分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "图片名称")
    @ExcelProperty(value = "图片名称")
    private String pictureName;
    @ApiModelProperty(value = "缩略图地址")
    @ExcelProperty(value = "缩略图地址")
    private String thumbPath;
    @ApiModelProperty(value = "图片地址")
    @ExcelProperty(value = "图片地址")
    private String picturePath;
    @ApiModelProperty(value = "创建人")
    @ExcelProperty(value = "创建人")
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
