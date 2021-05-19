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
 * 功能描述：
 * 保存订单专用验证商品实体
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/19
 **/
@Data
@ApiModel(description = "ValidateGoodsDTO")
public class ValidateGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品主键")
    private Long id;

    @ApiModelProperty(value = "商品分类一级id")
    private Long firstGcId;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品上下架状态（默认0:下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodsStatus;

    @ApiModelProperty(value = "是否支持快递 0不支持 1支持")
    private Integer expressFlag;

    @ApiModelProperty(value = "运费模板id")
    private Long freightTemplateId;

    @ApiModelProperty(value = "specId")
    private Long specId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品规格编号")
    private String specSerial;

    @ApiModelProperty(value = "商品规格名称")
    private String specName;

    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "是否启用（默认0启用，1未启用）")
    private Integer isEnable;

    @ApiModelProperty(value = "是否是主规格（默认0不是，1是）")
    private Integer mainFlag;

    @ApiModelProperty(value = "规格上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer specShow;

    @ApiModelProperty(value = "商品规格属性值名称（中间用逗号隔开）")
    private String specAttrName;

    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;

}
