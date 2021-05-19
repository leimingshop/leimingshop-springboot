/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加秒杀活动商品分页实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(value = "添加秒杀活动商品分页实体")
public class AddSeckillGoodsPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "商品实际销量")
    private Integer saleNum;

    @ApiModelProperty(value = "商品库存")
    private Integer specStorage;

    @ApiModelProperty(value = "可选操作 0添加 1已添加")
    private Integer operationType = 0;

    @ApiModelProperty(value = "已参加活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private List<Integer> activityTypeList = new ArrayList<>();

}
