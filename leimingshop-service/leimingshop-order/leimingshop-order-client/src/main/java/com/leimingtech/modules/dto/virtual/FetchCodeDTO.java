/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.virtual;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子提货码
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Data
@ApiModel(value = "电子提货码")
public class FetchCodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "订单商品ID")
    private Long orderGoodsId;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "会员名称")
    private String memberName;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "规格ID")
    private Long specId;
    @ApiModelProperty(value = "商品货号")
    private String goodsSerial;
    @ApiModelProperty(value = "商品图片")
    private String goodsImage;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "核销截止日期")
    private Date expiryDate;
    @ApiModelProperty(value = "有效天数（-1为永久有效）")
    private Integer validDay;
    @ApiModelProperty(value = "电子提货码")
    private String fetchCode;
    @ApiModelProperty(value = "电子提货码二维码图片地址")
    private String fetchCodeImage;
    @ApiModelProperty(value = "提货码状态（默认0待核销，1:部分核销，2:已核销，3:已过期）")
    private Integer codeStatus;
    @ApiModelProperty(value = "已核销数量")
    private Integer exchangeNum;
}
