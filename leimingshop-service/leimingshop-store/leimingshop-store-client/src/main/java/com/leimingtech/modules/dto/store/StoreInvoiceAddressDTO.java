/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺发票地址信息
 *
 * @author xuzhch
 * @since v1.0.0 2020-03-27
 */
@Data
@ApiModel(description = "StoreInvoiceAddressDTO")
public class StoreInvoiceAddressDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "收件人")
    private String addressee;
    @ApiModelProperty(value = "收件人电话")
    private String addresseePhone;
    @ApiModelProperty(value = "邮寄地址（空格分隔）")
    private String mailingAddress;
    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;
}