/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺商品发票设置表
 *
 * @author xuzhch
 * @date 2020年5月14日11:50:36
 */
@Data
@ApiModel(description = "StoreGoodsInvoiceSettingDTO")
public class StoreGoodsInvoiceSettingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否允许开具发票（0：不允许，1：允许）")
    private Integer invoiceFlag;
    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)")
    private String invoiceType;
    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）")
    private String invoiceContent;

}