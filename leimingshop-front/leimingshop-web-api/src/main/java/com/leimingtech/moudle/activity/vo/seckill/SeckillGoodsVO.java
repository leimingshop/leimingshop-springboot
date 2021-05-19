/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.vo.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 秒杀商品信息VO
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/16 13:44
 **/
@Data
@ApiModel(value = "秒杀商品")
public class SeckillGoodsVO implements Serializable {

    private static final long serialVersionUID = 3555029021714572285L;

    @ApiModelProperty("商品spu id")
    private Long id;

    @ApiModelProperty("商品sku id")
    private Long specId;

    @ApiModelProperty("商品主图")
    private String goodsMainPicture;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty(value = "活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "活动库存")
    private Integer activityStorage;

    @ApiModelProperty(value = "活动剩余库存")
    private Integer activitySurplusStorage;

    @ApiModelProperty(value = "已抢百分比")
    private BigDecimal salePercentage;
}
