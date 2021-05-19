/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.order;

import com.leimingtech.modules.dto.order.OrderGoodsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * PC端订单列表VO
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-05-18 14:41
 **/
@Data
@ToString
public class PCOrderPageVO implements Serializable {
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

    @ApiModelProperty(value = "支付自动取消时间")
    private Date cancalDate;

    @ApiModelProperty("当前时间")
    private Long currentTime;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "支付单号")
    private Long paySn;

    @ApiModelProperty(value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer orderStatus;

    @ApiModelProperty(value = "前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer appStatus;

    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    private Integer devlierType;

    @ApiModelProperty(value = "用户提交抬头类型（默认0：不开票、1：个人、2：单位）")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "退换货标记（默认0：没有退换货，1：有退换货）")
    private Integer afterFlag;

    @ApiModelProperty(value = "评价状态 0为评价，1已评价")
    private Integer evaluateStatus;

    @ApiModelProperty(value = "评价时间")
    private Date evaluateTime;

    @ApiModelProperty(value = "是否拆单（默认0:否,1:是）")
    private Integer isSplit;

    @ApiModelProperty(value = "父订单号")
    private Long parentOrderSn;

    @ApiModelProperty(value = "商品总数")
    private Integer totalNum;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "拼团记录id")
    private Long groupRecordId;

    @ApiModelProperty(value = "拼团成团超时时间")
    private Date groupOverTime;

    @ApiModelProperty(value = "拼团状态(0:进行中，1:成功，2:失败)")
    private Integer groupStatus;

    @ApiModelProperty(value = "拼团所需人数")
    private Integer groupNeedNum;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "订单商品集合")
    private List<OrderGoodsDTO> orderGoodsDTOList;

    @ApiModelProperty(value = "子订单集合")
    private List<PCOrderPageVO> orderChildrenList;

    @ApiModelProperty(value = "运费价格")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "支付方式名称")
    private String paymentName;

    @ApiModelProperty(value = "收货人姓名")
    private String trueName;

    public Integer getTotalNum() {
        totalNum = 0;
        if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                totalNum += orderGoodsDTO.getGoodsNum();
            }
        }
        return totalNum;
    }

}
