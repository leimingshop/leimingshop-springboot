/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提交订单校验未通过的商品信息DTO
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/8/27 20:25
 **/
@Data
@ToString
@ApiModel("校验失败的商品信息")
public class ErrorGoodsMsgDTO implements Serializable {

    private static final long serialVersionUID = 5738361075565752032L;

    @ApiModelProperty("商品ID")
    private Long spuId;

    @ApiModelProperty("规格ID")
    private Long skuId;

//    @ApiModelProperty("错误信息提示 eg:比加入时，单价提高了￥10.3")
//    private String errorMsg;
//
//    @ApiModelProperty("错误状态码 10:超过购买限制 20:库存不足 30:价格变更 40:下架状态")
//    private Integer errorStatus;

    /**
     * 主图
     */
    @ApiModelProperty("商品图片")
    private String goodsImage;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品规格名称")
    private String specName;

    /**
     * 商品规格属性和属性值名称（key:value）
     */
    @ApiModelProperty(value = "商品规格属性和属性值名称（key:value）")
    private String specAttrValueName;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;

    /**
     * 店铺id
     */
    @ApiModelProperty("店铺id")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String storeName;

    /**
     * 商品的购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsNum;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;
}
