/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品信息
 *
 * @author chengqian
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@ApiModel(description = "PcOrderGoodsInfoVO")
public class PcOrderGoodsInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单商品ID")
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty("商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品规格价格 ")
    private BigDecimal specPrice;

    @ApiModelProperty(value = "商品规格实际成交价")
    private BigDecimal specPayPrice;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("该商品的评价数量")
    private Integer num;
}
