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
 * @Author: SWH ab4856812@163.com
 * @Description: 售后审核分页列表数据
 * @Date: 2019/8/16 14:00
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AftersaleAuditLogPageDTO")
public class AftersaleAuditLogPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申请单号")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "商品货号")
    private String specSerial;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "规格Id")
    private String specId;
    @ApiModelProperty("规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "商户名称")
    private String storeName;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "申请时间")
    private Date createDate;
    @ApiModelProperty(value = "审核时间")
    private Date auditDate;
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "审核状态（1:审核通过,2:审核不通过,3:待审核,4:用户取消）")
    private Integer result;
    @ApiModelProperty(value = "审核状态（用于进入详情页判断状态 1:商家审核中,2:平台审核中,3:已完成,4:已取消）")
    private Integer auditStatus;
    @ApiModelProperty(value = "审核流程（1:商家审核,2:平台审核）")
    private Long process;
    @ApiModelProperty(value = "售后类型（0:退货,1:换货,2:仅退款）")
    private Integer aftersaleType;

}