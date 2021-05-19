/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.price;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSpecPriceAndStorageListDTO")
public class GoodsSpecPriceAndStorageListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品规格价格和库存集合
     */
    @ApiModelProperty(value = "商品规格价格和库存集合")
    private List<GoodsSpecPriceAndStorageDTO> goodsSpecPriceAndStorageDTOList;
    /**
     * 规格属性名集合
     */
    @ApiModelProperty(value = "规格属性名集合")
    private List<SpecAttrNameDTO> specAttrNameDTOList;

}