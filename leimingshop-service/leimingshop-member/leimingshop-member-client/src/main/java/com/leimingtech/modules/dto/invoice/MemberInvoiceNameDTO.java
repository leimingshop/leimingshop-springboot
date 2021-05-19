/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户发票名称
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
@Data
@ApiModel(description = "MemberInvoiceNameDTO")
public class MemberInvoiceNameDTO {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "企业名称（公司抬头）")
    private String company;
    @ApiModelProperty(value = "是否默认（默认0：不默认、1：默认）")
    private Integer defaultFlag;
}
