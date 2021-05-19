/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.transport;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 运费模板
 *
 * @author zhangtai zhangtai@leimingtech.com
 * @since 1.0.0 2019-05-14
 */
@Data
public class TransportExcel {
    @ExcelProperty(value = "运费模板ID")
    private Long id;
    @ExcelProperty(value = "运费模板名称")
    private String title;
    @ExcelProperty(value = "发货地区模板ID")
    private String sendTplId;
    @ExcelProperty(value = "店铺ID")
    private String storeId;
    @ExcelProperty(value = "默认标记(0为默认运费模板,1为非默认运费模板)")
    private Integer isDefault;
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
