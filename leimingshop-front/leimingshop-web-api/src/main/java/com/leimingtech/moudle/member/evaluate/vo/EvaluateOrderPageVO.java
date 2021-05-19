/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.evaluate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Front端订单列表DTO
 *
 * @author chengqian
 * @version V1.0
 * @date 2019/7/9 11:22
 **/
@Data
@ToString
public class EvaluateOrderPageVO implements Serializable {
    private static final long serialVersionUID = 950745639568025495L;

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

    @ApiModelProperty(value = "收货人姓名")
    private String buyerName;


    @ApiModelProperty(value = "支付总金额")
    private BigDecimal orderAmount;


    @ApiModelProperty(value = "评价状态 0为评价，1已评价")
    private Integer evaluateStatus;

    @ApiModelProperty(value = "评价时间")
    private Date evaluateTime;


    @ApiModelProperty(value = "商品总数")
    private Integer totalNum;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "订单商品集合")
    private List<OrderGoodsVO> orderGoodsDTOList;

}
