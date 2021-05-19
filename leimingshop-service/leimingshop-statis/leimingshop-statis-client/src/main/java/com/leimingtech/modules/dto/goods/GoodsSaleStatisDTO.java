/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品销量统计信息
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(description = "GoodsSaleStatisDTO")
public class GoodsSaleStatisDTO implements Serializable {

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品货号")
    private Long goodsSerial;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "浏览量")
    private Integer pageView;

    @ApiModelProperty(value = "浏览人数")
    private Integer visitorsNumber;

    @ApiModelProperty(value = "付款人数")
    private Integer paymentNumber;

    @ApiModelProperty(value = "单品转化率")
    private Double singleConversion;

    @ApiModelProperty(value = "销售数量")
    private Integer salesNumber;

    @ApiModelProperty(value = "销售金额")
    private BigDecimal salesAmount;

}
