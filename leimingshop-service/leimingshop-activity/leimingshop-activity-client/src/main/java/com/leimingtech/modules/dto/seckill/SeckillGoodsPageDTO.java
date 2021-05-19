/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <秒杀商品分页展示实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/13
 */
@Data
@ApiModel("秒杀商品分页展示实体")
public class SeckillGoodsPageDTO {

    @ApiModelProperty("商品spu id")
    private Long id;

    @ApiModelProperty("商品sku id")
    private Long specId;

    @ApiModelProperty("商品主图")
    private String goodsMainPicture;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品副标题")
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

    public BigDecimal getSalePercentage() {
        if (activityStorage != null && activitySurplusStorage != null) {
            if (activityStorage.equals(0)) {
                return BigDecimal.valueOf(100).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                BigDecimal buyNum = BigDecimal.valueOf(activityStorage - activitySurplusStorage);
                BigDecimal totalNum = BigDecimal.valueOf(activityStorage);
                return buyNum.multiply(BigDecimal.valueOf(100)).divide(totalNum, 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return null;
        }
    }

}
