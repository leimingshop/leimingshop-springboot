/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.favorities.vo.favorites;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName FavoritesPageVO
 * @Description
 * @Author chengqian
 * @Date 2019-07-22 16:20
 * @Version 1.0
 */
@Data
@ApiModel(description = "收藏信息")
public class FavoritesPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收藏id")
    private Long id;

    @ApiModelProperty(value = "收藏时的商品价格", required = true)
    private BigDecimal favPrice;

    @ApiModelProperty("会员ID")
    private Long memberId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品默认对应的规格id")
    private Long specId;

    @ApiModelProperty(value = "售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品上下架状态（默认0:下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "店铺类型 1 自营 2 普通")
    private Integer storeType;

    @ApiModelProperty(value = "商品图片")
    private String pictureUrl;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "副标题")
    private String goodsSubTitle;

    @ApiModelProperty(value = "商品标签")
    private String labelName;

}
