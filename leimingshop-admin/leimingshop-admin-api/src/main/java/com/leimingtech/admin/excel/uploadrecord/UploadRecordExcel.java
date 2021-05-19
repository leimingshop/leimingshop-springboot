/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.uploadrecord;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 文件上传记录表
 *
 * @since 7.0 2019-05-24
 */
@Data
public class UploadRecordExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "文件路径url")
    private String fileUrl;
    @ExcelProperty(value = "文件真实名称")
    private String fileRealName;
    @ExcelProperty(value = "文件后缀")
    private String fileSuffix;
    @ExcelProperty(value = "文件大小（KB为单位）")
    private Integer fileSize;
    @ExcelProperty(value = "业务分类（在什么场景下上传）")
    private String uploadType;
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
