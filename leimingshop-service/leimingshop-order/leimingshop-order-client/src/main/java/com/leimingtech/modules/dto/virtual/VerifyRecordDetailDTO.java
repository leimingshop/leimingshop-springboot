/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.virtual;

import com.leimingtech.modules.dto.order.AdminOrderDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 核销记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Data
@ApiModel(value = "核销记录详情")
public class VerifyRecordDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "订单商品ID")
    private Long orderGoodsId;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "商品图片")
    private String goodsImage;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "商品货号")
    private String goodsSerial;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "会员名称")
    private String memberName;
    @ApiModelProperty(value = "用户手机号")
    private String memberMobile;
    @ApiModelProperty(value = "核销数量")
    private Integer exchangeNum;
    @ApiModelProperty(value = "电子提货码")
    private String fetchCode;
    @ApiModelProperty(value = "核销人")
    private String creator;
    @ApiModelProperty(value = "核销时间")
    private Date createDate;
    @ApiModelProperty(value = "订单信息")
    private AdminOrderDetailDTO adminOrderDetailDTO;
}
