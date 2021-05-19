/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品列表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-6
 */
@Data
@ApiModel(description = "GoodsPageDTO")
public class GoodsPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品Id")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品销售价格")
    private BigDecimal goodsSellPrice;
    @ApiModelProperty(value = "所属分类")
    private String categoty;
    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;
    @ApiModelProperty(value = "是否选中")
    private Boolean checked;

}