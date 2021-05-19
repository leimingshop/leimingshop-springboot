/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.leimingtech.modules.dto.invoice.OrderInvoiceSaveDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 功能描述：
 * <>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/17 18:17
 **/
@Data
@ApiModel(description = "InsertCartOrderDTO")
public class InsertCartOrderDTO {

    @ApiModelProperty("收货地址id")
    private Long addressId;

    @ApiModelProperty("会员优惠券id json {storeId:memberCouponsId}")
    private String memberCouponsId;

    @ApiModelProperty("订单留言集合")
    private String orderMessage;

    @ApiModelProperty("订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer orderPlatform;

    @ApiModelProperty("订单发票信息")
    private List<OrderInvoiceSaveDTO> orderInvoiceSaveDTOS;

    @ApiModelProperty(value = "收件人(虚拟订单必传)")
    private String memberName;

    @ApiModelProperty(value = "用户手机号(虚拟订单必传)")
    private String memberMobile;

    @ApiModelProperty(value = "是否使用余额付款 0不使用 1使用")
    private Integer useBalance;

    @ApiModelProperty("使用余额支付时，必传支付密码")
    private String payPassword;

}
