/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: taotie
 * @Date: 2020/6/15 18:19
 * @email: lishuo@leimingtech.com
 * @Description:后台z展示分类模板
 */
@Data
public class GoodsClassCustomTemplateExcel implements Serializable {
    @ExcelProperty("排序")
    private Integer sort;
    @ExcelProperty("一级展示分类名称")
    private String firstShowGcName;
    @ExcelProperty("二级展示分类的名称")
    private String secondShowGcName;
    @ExcelProperty("三级展示分类名称")
    private String thirdShowGcName;
    @ExcelProperty("关联后台一级分类")
    private String firstGcName;
    @ExcelProperty("关联后台二级分类")
    private String secondGcName;
    @ExcelProperty("关联后台三级分类")
    private String thirdGcName;
    @ExcelProperty("关联图片地址")
    private String picUrl;
    @ExcelProperty("展示类型 0 H5展示 1 PC展示")
    private Integer showType;
    @ExcelProperty("是否展示0不展示1展示")
    private Integer showFlag;
}