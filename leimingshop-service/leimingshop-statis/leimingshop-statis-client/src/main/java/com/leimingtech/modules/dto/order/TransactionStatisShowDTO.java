/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易统计展示对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(description = "TransactionStatisShowDTO")
public class TransactionStatisShowDTO implements Serializable {

    @Id
    private Long id;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "浏览量")
    private Integer pageView;

    @ApiModelProperty(value = "访客数")
    private Integer visitorsNumber;

    @ApiModelProperty(value = "下单人数")
    private Integer submitOrderNumber;

    @ApiModelProperty(value = "订单数")
    private Integer orderNumber;

    @ApiModelProperty(value = "下单件数")
    private Integer orderGoodsNumber;

    @ApiModelProperty(value = "下单金额")
    private BigDecimal submitOrderAmount;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "付款人数")
    private Integer paymentNumber;

    @ApiModelProperty(value = "付款订单数")
    private Integer paymentOrderNumber;

    @ApiModelProperty(value = "付款件数")
    private Integer paymentGoodsNumber;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "客单价")
    private BigDecimal perTicketSales;

    @ApiModelProperty(value = "创建时间（日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（月）")
    private Date createMonthTime;

    @ApiModelProperty(value = "下单转换率")
    private Double submitConversion;

    @ApiModelProperty(value = "付款转化率")
    private Double paymentConversion;

    @ApiModelProperty(value = "成交转化率")
    private Double closingConversion;

    /**
     * 初始化参数
     *
     * @return
     */
    public static TransactionStatisShowDTO newTransactionStatisShowDTO() {
        TransactionStatisShowDTO transactionStatisShowDTO = new TransactionStatisShowDTO();
        transactionStatisShowDTO.setPageView(0);
        transactionStatisShowDTO.setVisitorsNumber(0);
        transactionStatisShowDTO.setPerTicketSales(BigDecimal.ZERO);
        transactionStatisShowDTO.setRefundAmount(BigDecimal.ZERO);
        transactionStatisShowDTO.setSubmitOrderAmount(BigDecimal.ZERO);
        transactionStatisShowDTO.setPaymentAmount(BigDecimal.ZERO);
        transactionStatisShowDTO.setOrderGoodsNumber(0);
        transactionStatisShowDTO.setOrderNumber(0);
        transactionStatisShowDTO.setPaymentGoodsNumber(0);
        transactionStatisShowDTO.setSubmitOrderNumber(0);
        transactionStatisShowDTO.setPaymentNumber(0);
        transactionStatisShowDTO.setPaymentOrderNumber(0);
        transactionStatisShowDTO.setPaymentConversion(0.0);
        transactionStatisShowDTO.setSubmitConversion(0.0);
        transactionStatisShowDTO.setClosingConversion(0.0);
        return transactionStatisShowDTO;
    }
}
