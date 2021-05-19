/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.transport;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运费模板扩展表
 *
 * @author zhangtai zhangtai@leimingtech.com
 * @since 1.0.0 2019-05-14
 */
@Data
public class TransportExtendExcel {
    @ExcelProperty(value = "运费模板扩展ID")
    private Long id;
    @ExcelProperty(value = "平邮py 快递kd EMS es")
    private String type;
    @ExcelProperty(value = "市级地区ID组成的串，以，隔开，两端也有，")
    private String areaId;
    @ExcelProperty(value = "省级地区ID组成的串，以，隔开，两端也有，")
    private String topAreaId;
    @ExcelProperty(value = "地区name组成的串，以，隔开")
    private String areaName;
    @ExcelProperty(value = "首件数量")
    private Integer snum;
    @ExcelProperty(value = "首件运费")
    private BigDecimal sprice;
    @ExcelProperty(value = "续件数量")
    private Integer xnum;
    @ExcelProperty(value = "续件运费")
    private BigDecimal xprice;
    @ExcelProperty(value = "是否默认运费1是2否")
    private Integer isDefault;
    @ExcelProperty(value = "运费模板ID")
    private String transportId;
    @ExcelProperty(value = "运费模板")
    private String transportTitle;
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
