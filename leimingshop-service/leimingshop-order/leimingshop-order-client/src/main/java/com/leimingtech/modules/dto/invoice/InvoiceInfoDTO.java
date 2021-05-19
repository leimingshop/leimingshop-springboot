/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发票信息表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Data
@ApiModel(value = "发票信息表")
public class InvoiceInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "发票表ID")
    private Long invoiceId;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品编号")
    private Long spu;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "规格ID")
    private Long specId;
    @ApiModelProperty(value = "规格描述")
    private String specInfo;
    @ApiModelProperty(value = "商品规格价格")
    private BigDecimal specPayPrice;
    @ApiModelProperty(value = "商品总价格")
    private BigDecimal specTotalPrice;
    @ApiModelProperty(value = "一级分类ID")
    private Long firstGcId;
    @ApiModelProperty(value = "一级分类名称")
    private String firstGcName;
}
