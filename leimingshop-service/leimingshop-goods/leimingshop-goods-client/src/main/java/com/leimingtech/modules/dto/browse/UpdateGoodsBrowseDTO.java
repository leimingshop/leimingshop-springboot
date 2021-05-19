/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.browse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 更新足迹记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */
@Data
@ApiModel(description = "UpdateGoodsBrowseDTO")
public class UpdateGoodsBrowseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "足迹id")
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long memberId;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品图片")
    private String goodsImage;
    @ApiModelProperty(value = "商品价格")
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "商品规格Id")
    private Long goodsSpecId;
}