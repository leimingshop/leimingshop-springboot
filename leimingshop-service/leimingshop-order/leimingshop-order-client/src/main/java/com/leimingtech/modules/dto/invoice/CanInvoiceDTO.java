/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 可以开票的发票类型和发票内容
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Data
@ApiModel(description = "CanInvoiceDTO")
public class CanInvoiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否可以开票")
    private Boolean flag;
    @ApiModelProperty(value = "可开票的发票类型")
    private List<String> invoiceTypes;
    @ApiModelProperty(value = "可开票的发票内容")
    private List<String> invoiceContents;

}