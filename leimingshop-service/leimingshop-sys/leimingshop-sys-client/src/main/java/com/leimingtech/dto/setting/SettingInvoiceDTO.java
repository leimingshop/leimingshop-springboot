/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author chengqian
 * @Description 发票设置
 * @Date 17:05 2020-10-15
 * @Param
 * @return
 */
@Data
@ApiModel(description = "SettingInvoiceDTO")
public class SettingInvoiceDTO implements Serializable {

    @ApiModelProperty(value = "开具发票时间")
    private Integer openInvoice;

    @ApiModelProperty(value = "换开发票时间")
    private Integer exchangeInvoice;
}
