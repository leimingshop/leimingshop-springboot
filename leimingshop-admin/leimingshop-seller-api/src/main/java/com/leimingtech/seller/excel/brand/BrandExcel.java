/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.excel.brand;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 品牌管理
 * @Date :2019/5/20 14:11
 * @Version V1.0
 **/
@Data
public class BrandExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "品牌名称")
    private String brandName;
    @ExcelProperty(value = "品牌英文名称")
    private String brandNameEn;
    @ExcelProperty(value = "品牌首字母")
    private String brandInitials;
    @ExcelProperty(value = "图片")
    private String brandPic;
    @ExcelProperty(value = "排序")
    private Integer brandSort;
    @ExcelProperty(value = "店铺ID")
    private Long storeId;
    @ExcelProperty(value = "品牌申请，0为申请中，1为通过，默认为1，申请功能是会员使用，系统后台默认为1")
    private Integer brandApply;
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
