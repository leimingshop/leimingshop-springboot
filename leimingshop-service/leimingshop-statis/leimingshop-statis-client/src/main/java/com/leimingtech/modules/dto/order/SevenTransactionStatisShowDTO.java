/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 近七日交易统计数据
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(description = "SevenTransactionStatisShowDTO")
public class SevenTransactionStatisShowDTO implements Serializable {

    @ApiModelProperty(value = "浏览量")
    private Integer pageView;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "下单转换率")
    private Double submitConversion;

    @ApiModelProperty(value = "付款转化率")
    private Double paymentConversion;

    @ApiModelProperty(value = "成交转化率")
    private Double closingConversion;

    @ApiModelProperty(value = "创建时间（日）")
    private Date createDayTime;

    @ApiModelProperty(value = "下单人数")
    private Integer subOrderNumber;

    @ApiModelProperty(value = "付款人数")
    private Integer payOrderNumber;

    @ApiModelProperty(value = "浏览人数")
    private Integer uvCount;

    public static SevenTransactionStatisShowDTO infoSevenTransactionStatisShowDTO() {
        SevenTransactionStatisShowDTO sevenTransactionStatisShowDTO = new SevenTransactionStatisShowDTO();
        sevenTransactionStatisShowDTO.setRefundAmount(new BigDecimal(0));
        sevenTransactionStatisShowDTO.setRefundAmount(new BigDecimal(0));
        sevenTransactionStatisShowDTO.setPaymentConversion((double) 0);
        sevenTransactionStatisShowDTO.setSubmitConversion((double) 0);
        sevenTransactionStatisShowDTO.setClosingConversion((double) 0);
        sevenTransactionStatisShowDTO.setPayOrderNumber(0);
        sevenTransactionStatisShowDTO.setSubOrderNumber(0);
        sevenTransactionStatisShowDTO.setUvCount(0);
        return sevenTransactionStatisShowDTO;
    }
}
