/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 支付方式表信息导出
 * @Date :2019/5/20 14:51
 * @Version V1.0
 **/
@Data
public class PaymentExcel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键")
    @ExcelProperty(value = "主键")
    private Long id;
    /**
     * 支付标识
     */
    @ApiModelProperty(value = "支付标识")
    @ExcelProperty(value = "支付标识")
    private String paymentCode;
    /**
     * 支付对接配置信息（json格式）
     */
    @ApiModelProperty(value = "支付对接配置信息（json格式）")
    @ExcelProperty(value = "支付对接配置信息（json格式）")
    private String paymentConfig;
    /**
     * 状态（默认0禁用，1启用）
     */
    @ApiModelProperty(value = "状态（默认0禁用，1启用）")
    @ExcelProperty(value = "状态（默认0禁用，1启用）")
    private Integer paymentState;
    /**
     * 支付图标
     */
    @ApiModelProperty(value = "支付图标")
    @ExcelProperty(value = "支付图标")
    private String paymentLogo;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @ExcelProperty(value = "创建人")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @ExcelProperty(value = "更新人")
    private String updater;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
