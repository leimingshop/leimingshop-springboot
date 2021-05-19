/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.settle;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账汇总表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@ApiModel(description = "BillTotalDTO")
public class BillTotalDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "对账汇总单编号")
    private String billTotalSn;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名")
    private String storeName;
    @ApiModelProperty(value = "本期应结金额")
    private BigDecimal resultTotals;
    @ApiModelProperty(value = "店铺类型(1 自营2 普通)")
    private Integer storeType;
    @ApiModelProperty(value = "商家确认状态（0 未确认 1 已确认）")
    private Integer confirmStatus;
    @ApiModelProperty(value = "结算状态 0未结算1已结算")
    private Integer billState;
    @ApiModelProperty(value = "开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date obtStartTime;
    @ApiModelProperty(value = "结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date obtEndTime;
    @ApiModelProperty(value = "出账日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date billTime;
    @ApiModelProperty(value = "结算备注")
    private String billRemark;
    @ApiModelProperty(value = "商家备注")
    private String storeRemark;
    @ApiModelProperty(value = "佣金总金额")
    private BigDecimal commisTotal;

    @ApiModelProperty(value = "退还总佣金")
    private BigDecimal returnCommisTotal;

    @ApiModelProperty(value = "店铺活动总费用")
    private BigDecimal storeCostTotal;

    @ApiModelProperty(value = "积分抵扣总金额")
    private BigDecimal pointTotal;

    @ApiModelProperty(value = "退款总金额")
    private BigDecimal returnAmount;
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderTotalAmount;
}