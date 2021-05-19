/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.brand;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.leimingtech.commons.tools.utils.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 品牌管理
 * @Date :2019/5/20 14:11
 * @Version V1.0
 **/
@Data
public class BrandExcelDTO {

    @ExcelProperty("编号")
    private Long id;
    @ExcelProperty("品牌名称")
    private String brandName;
    @ExcelProperty("品牌英文名称")
    private String brandNameEn;
    @ExcelProperty("品牌首字母")
    private String brandInitials;
    @ExcelProperty("品牌logo")
    private String brandPic;
    /**
     * 0为申请中，1为通过，默认为1，申请功能是会员使用，系统后台默认为
     */
    @ExcelIgnore
    private String brandApply;
    @ExcelIgnore
    private Date updateDate;

    @ExcelIgnore
    private String updateDateStr;

    public String getUpdateDateStr() {
        return DateUtils.format(updateDate, DateUtils.DATE_TIME_PATTERN);
    }

}
