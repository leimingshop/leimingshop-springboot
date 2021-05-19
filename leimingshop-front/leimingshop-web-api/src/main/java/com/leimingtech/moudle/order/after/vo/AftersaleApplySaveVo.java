/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.after.vo;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 售后申请保存
 *
 * @author xuzhch
 * @version V1.0
 * @date 2019/6/18 21:26
 */
@Data
@ApiModel(description = "AftersaleApplySaveVo")
public class AftersaleApplySaveVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    @NotNull(message = "订单ID不能为空", groups = AddGroup.class)
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    @NotNull(message = "订单编号不能为空", groups = AddGroup.class)
    private Long orderSn;
    @ApiModelProperty(value = "订单项ID")
    @NotNull(message = "订单商品ID不能为空", groups = AddGroup.class)
    private Long orderGoodsId;
    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "用户昵称")
    private String memberName;
    @ApiModelProperty(value = "售后方式（0:退货,1:换货）")
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
    @ApiModelProperty(value = "收货人姓名")
    @Length(max = 20, message = "收货人姓名不能超过20位", groups = AddGroup.class)
    private String receiver;
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;
    @ApiModelProperty(value = "收货人地址")
    private String receiverAddress;
    @ApiModelProperty(value = "联系人")
    @Length(max = 20, message = "联系人名称不能超过20位", groups = AddGroup.class)
    private String contacts;
    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;
    @ApiModelProperty(value = "是否开发票（0:否,1:是）")
    private Integer isInvoice;

}