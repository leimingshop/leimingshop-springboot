/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName CmsFrontGoodsSearchDTO
 * @Description 指定商品分页搜索
 * @Author yuhaifeng
 * @Date 2020/4/9 15:12
 * @Version 1.0
 **/
@Data
@ApiModel(value = "指定商品分页搜索")
public class CmsFrontGoodsSearchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "商品规格id")
    private Long specId;

}
