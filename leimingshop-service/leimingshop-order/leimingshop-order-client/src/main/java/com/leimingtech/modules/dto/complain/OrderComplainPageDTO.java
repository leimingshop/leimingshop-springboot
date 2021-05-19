/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.complain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 订单投诉分页
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "订单投诉分页")
public class OrderComplainPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "投诉商品信息")
    private List<OrderComplainGoodsDetail> list;

    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "投诉类型1:服务相关,2:物流相关,3:售后相关,4:商品相关5:其他")
    private Integer type;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "处理时间")
    private Date createDate;

}