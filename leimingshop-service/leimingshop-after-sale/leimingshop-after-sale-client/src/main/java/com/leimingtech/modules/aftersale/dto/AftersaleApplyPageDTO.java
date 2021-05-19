/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleApplyPageDTO")
public class AftersaleApplyPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "商品货号")
    private String specSerial;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "规格ID")
    private String specId;
    @ApiModelProperty("规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;
    @ApiModelProperty(value = "审核状态（1:商家审核中,2:平台审核中,3:已完成,4:已取消）")
    private String auditStatus;
    @ApiModelProperty(value = "审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）")
    private String auditResult;
    @ApiModelProperty(value = "售后类型（0:退货,1:换货,2:仅退款）")
    private String aftersaleType;
    @ApiModelProperty(value = "审核时间")
    private Date auditDate;
}
