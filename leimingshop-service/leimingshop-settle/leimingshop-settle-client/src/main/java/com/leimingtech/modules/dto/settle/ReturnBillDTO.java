/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.settle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退货结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@ApiModel(description = "ReturnBillDTO")
public class ReturnBillDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "结算单编号")
    private Long billTotalId;
    @ApiModelProperty(value = "退货编号")
    private Long returnSn;
    @ApiModelProperty(value = "申请时间")
    private Date returnTime;
    @ApiModelProperty(value = "退款金额")
    private BigDecimal returnAmount;
    @ApiModelProperty(value = "规格编号")
    private String specSerial;
    @ApiModelProperty(value = "规格ID")
    private Long specId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品图片")
    private String specMainPicture;
    @ApiModelProperty(value = "支付方式")
    private String paymentName;
    @ApiModelProperty(value = "平台处理时间")
    private Date manageTime;
}