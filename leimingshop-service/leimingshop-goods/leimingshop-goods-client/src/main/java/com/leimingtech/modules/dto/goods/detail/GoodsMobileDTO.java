/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.detail;

import com.leimingtech.modules.dto.goods.spec.GoodsAttributeInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsMobileDTO")
public class GoodsMobileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品规格名称")
    private String specName;

    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品规格属性值名称（中间用逗号隔开）")
    private String specAttrName;

    @ApiModelProperty(value = "商品描述")
    private String mobileBody;


    @ApiModelProperty(value = "销售数量")
    private Integer saleNum;

    @ApiModelProperty(value = "商品图片集合")
    private List<SpecAttributePictureUrlDTO> specAttributePictureUrlDTOList;

    @ApiModelProperty(value = "商品属性集合")
    private List<GoodsAttributeInfoDTO> goodsAttributeInfoDTOList;

}