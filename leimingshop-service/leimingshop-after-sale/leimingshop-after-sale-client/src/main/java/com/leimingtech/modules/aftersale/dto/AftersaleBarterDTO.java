/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 订单换货表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleBarterDTO")
public class AftersaleBarterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "服务单号")
    private Long serviceSn;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "售后物流状态（1为买家待发货,2为卖家待收货,3卖家已收货,4为买家待收货,5为已完成）")
    private Integer logisticsStatus;
    @ApiModelProperty(value = "售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:确认收货,5:退款中,6:退款成功,7:换货失败,8:待换货入库,9:换货出库中,10:换货成功）")
    private Integer aftersaleStatus;
    @ApiModelProperty(value = "支付方式")
    private Long payType;
    @ApiModelProperty(value = "联系人")
    private String contacts;
    @ApiModelProperty(value = "联系电话")
    private String contactsPhone;
    @ApiModelProperty(value = "收货人姓名")
    private String receiver;
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;
    @ApiModelProperty(value = "收货人地址")
    private String receiverAddress;
    @ApiModelProperty(value = "买家发货时间")
    private Date buyerDeliveryTime;
    @ApiModelProperty(value = "买家发货物流公司")
    private String buyerLogisticsCompany;
    @ApiModelProperty(value = "买家发货物流单号")
    private String buyerLogisticsNumber;
    @ApiModelProperty(value = "买家发货联系电话")
    private String buyerLogisticsContactsPhone;
    @ApiModelProperty(value = "商家发货单号")
    private Long sellerDeliveryNo;
    @ApiModelProperty(value = "商家发货时间")
    private Date sellerDeliveryTime;
    @ApiModelProperty(value = "商家发货物流公司")
    private String sellerLogisticsCompany;
    @ApiModelProperty(value = "商家发货物流单号")
    private String sellerLogisticsNumber;
    @ApiModelProperty(value = "退换货时间")
    private Date createDate;
}
