/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.order;

import com.leimingtech.modules.dto.order.OrderGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * PC端发票列表VO
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-05-18 14:41
 **/
@Data
@ToString
@ApiModel(description = "PCOrderInvoicePageVO")
public class PCOrderInvoicePageVO implements Serializable {
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

    @ApiModelProperty("当前时间")
    private Long currentTime;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer orderStatus;

    @ApiModelProperty(value = "前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer appStatus;

    @ApiModelProperty(value = "用户提交抬头类型（默认0：不开票、1：个人、2：单位）")
    private Integer companyType;
    @ApiModelProperty(value = "用户提交发票类型(1：电子、2：纸质、3：增值税)")
    private Integer memberInvoiceType;
    @ApiModelProperty(value = "商家开具发票类型(1：电子、2：纸质、3：增值税)")
    private Integer storeInvoiceType;
    @ApiModelProperty(value = "开票进度（1：待开票、2：已开票、3：待换开、4：已换开）")
    private Integer invoiceEvolve;

    @ApiModelProperty(value = "退换货标记（默认0：没有退换货，1：有退换货）")
    private Integer afterFlag;

    @ApiModelProperty(value = "是否拆单（默认0:否,1:是）")
    private Integer isSplit;

    @ApiModelProperty(value = "父订单号")
    private Long parentOrderSn;

    @ApiModelProperty(value = "商品总数")
    private Integer totalNum;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "订单商品集合")
    private List<OrderGoodsDTO> orderGoodsDTOList;

    @ApiModelProperty(value = "子订单集合")
    private List<PCOrderInvoicePageVO> orderChildrenList;

    @ApiModelProperty(value = "运费价格")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "支付方式名称")
    private String paymentName;

    @ApiModelProperty(value = "收货人姓名")
    private String trueName;

//    public Integer getTotalNum() {
//        totalNum = 0;
//        if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
//            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
//                totalNum += orderGoodsDTO.getGoodsNum();
//            }
//        }
//        return totalNum;
//    }

}
