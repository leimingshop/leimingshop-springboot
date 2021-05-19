/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改购物车数量
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ApiModel(description = "UpdateCartStatusDTO")
public class UpdateCartStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态,3，商品不存在）")
    private Integer status;
    @ApiModelProperty(value = "商品规格ID")
    private Long specId;
    @ApiModelProperty(value = "当前规格销售价")
    private BigDecimal specSellPrice;
    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;
    @ApiModelProperty(value = "活动剩余库存")
    private Integer activitySurplusStorage;
    @ApiModelProperty(value = "修改人")
    private String updateName;
    @ApiModelProperty(value = "是否允许开具发票（0：不允许，1：允许）")
    private Integer invoiceFlag;
    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3")
    private String invoiceType;
    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）1,2")
    private String invoiceContent;


}
