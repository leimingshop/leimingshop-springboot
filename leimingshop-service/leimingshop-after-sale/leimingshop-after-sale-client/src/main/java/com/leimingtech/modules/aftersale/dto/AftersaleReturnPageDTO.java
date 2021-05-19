/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleReturnPageDTO")
public class AftersaleReturnPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "余额退款金额")
    private BigDecimal balanceRefundAmount;
    @ApiModelProperty(value = "售后物流状态（1为买家待发货,2为卖家待收货,3卖家已收货,4为买家待收货,5为已完成）")
    private Integer logisticsStatus;
    @ApiModelProperty(value = "售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:确认收货,5:退款中,6:退款成功,7:换货失败,8:待换货入库,9:换货出库中,10:换货成功）")
    private Integer aftersaleStatus;
    @ApiModelProperty(value = "商品货号")
    private String specSerial;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "规格ID")
    private String specId;
    @ApiModelProperty("规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;
    @ApiModelProperty(value = "退款记录表id（用于退款失败重新发起退款操作）")
    private Long aftersaleReturnRecordId;
}
