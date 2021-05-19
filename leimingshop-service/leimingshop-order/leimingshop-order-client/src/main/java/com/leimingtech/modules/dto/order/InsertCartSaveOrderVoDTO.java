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
 * <购物车去结算提交订单超类实体>
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-07-08 20:21
 **/
@Data
@ApiModel(description = "InsertCartSaveOrderVoDTO")
public class InsertCartSaveOrderVoDTO {

    @ApiModelProperty("订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer orderPlatform;

    @ApiModelProperty("用户留言")
    private String orderMessage;

    @ApiModelProperty("地址id")
    private Long addressId;

    @ApiModelProperty("用户id")
    private Long buyerId;

    @ApiModelProperty("是否使用余额付款 0不使用 1使用")
    private Integer useBalance;

    @ApiModelProperty("订单发票信息")
    private List<OrderInvoiceSaveDTO> orderInvoiceSaveDTOS;

    @ApiModelProperty("会员优惠券id json {storeId:memberCouponsId}")
    private String memberCouponsId;

    @ApiModelProperty(value = "收件人(虚拟订单必传)")
    private String memberName;

    @ApiModelProperty(value = "用户手机号(虚拟订单必传)")
    private String memberMobile;

}
