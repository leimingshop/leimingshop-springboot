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
import java.math.BigDecimal;
import java.util.Date;


/**
 * 售后申请表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleApplyDTO")
public class AftersaleApplyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "订单项ID")
    private Long orderGoodsId;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "用户昵称")
    private String memberName;
    @ApiModelProperty(value = "售后方式（0:退货,1:换货,2:仅退款）")
    private Integer aftersaleType;
    @ApiModelProperty(value = "售后数量")
    private Integer applyNum;
    @ApiModelProperty(value = "申请售后原因ID")
    private Long aftersaleReasonId;
    @ApiModelProperty(value = "申请售后原因")
    private String aftersaleReason;
    @ApiModelProperty(value = "问题描述")
    private String aftersaleExplain;
    @ApiModelProperty(value = "凭证图片")
    private String aftersalePics;
    @ApiModelProperty(value = "审核状态（1:商家审核中,2:平台审核中,3:已完成,4:已取消,5:仲裁中）")
    private Integer auditStatus;
    @ApiModelProperty(value = "审核结果（0:未审核,1:审核通过,2:审核不通过,3:审核中,4:用户取消）")
    private Integer auditResult;
    @ApiModelProperty(value = "收货人姓名")
    private String receiver;
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;
    @ApiModelProperty(value = "收货人地址")
    private String receiverAddress;
    @ApiModelProperty(value = "联系人")
    private String contacts;
    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;
    @ApiModelProperty(value = "是否开发票（0:否,1:是）")
    private Integer isInvoice;
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;
}
