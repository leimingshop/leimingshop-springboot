/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.after.vo;

import com.leimingtech.modules.dto.order.AvailableAfterSaleOrderGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 可申请售后订单列表实体
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020/5/19 18:10
 */
@Data
@ApiModel(description = "AvailableAfterSaleOrderVo")
public class AvailableAfterSaleOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "买家ID")
    private Long buyerId;
    @ApiModelProperty(value = "买家名称")
    private String buyerName;
    @ApiModelProperty(value = "订单状态：0已取消;10待付款;20待发货;30待收货;40交易完成;")
    private Integer orderStatus;
    @ApiModelProperty(value = "订单完成时间")
    private Date completeTime;
    @ApiModelProperty(value = "是否拆单（默认0否,1是）")
    private Integer isSplit;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "是否超过售后期(默认0否,1是)")
    private Integer isOutTime;
    @ApiModelProperty(value = "订单商品列表")
    private List<AvailableAfterSaleOrderGoodsDTO> orderGoodsList;

}