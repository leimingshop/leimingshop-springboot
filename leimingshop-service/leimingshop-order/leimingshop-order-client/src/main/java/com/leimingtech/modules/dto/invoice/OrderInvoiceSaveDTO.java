/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "订单发票保存使用")
public class OrderInvoiceSaveDTO implements Serializable {
    @ApiModelProperty("订单ID")
    private Long orderId;
    @ApiModelProperty("店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "用户提交抬头类型（默认0：不开票、1：个人、2：单位）")
    private Integer companyType;
    @ApiModelProperty(value = "用户提交发票类型(1：电子、2：纸质、3：增值税)")
    private Integer memberInvoiceType;
    @ApiModelProperty(value = "用户提交发票内容（1：商品明细、2：商品类别）")
    private Integer memberInvoiceContent;
    @ApiModelProperty(value = "个人名称（个人发票使用）")
    private String personalName;
    @ApiModelProperty(value = "收票人名称")
    private String addresseeName;
    @ApiModelProperty(value = "收票人手机")
    private String addresseePhone;
    @ApiModelProperty(value = "收票人邮箱")
    private String addresseeMail;
    @ApiModelProperty(value = "收票人地址（空格分隔）")
    private String addresseeAddress;
    @ApiModelProperty(value = "收票人详细地址")
    private String detailedAddress;
    @ApiModelProperty(value = "省ID")
    private Long provinceId;
    @ApiModelProperty(value = "市ID")
    private Long cityId;
    @ApiModelProperty(value = "区ID")
    private Long districtId;
    @ApiModelProperty(value = "街道ID")
    private Long streetId;
    @ApiModelProperty(value = "企业名称（公司抬头）")
    private String company;
    @ApiModelProperty(value = "企业税号")
    private String dutyParagraph;
    @ApiModelProperty(value = "注册地址")
    private String registeredAddress;
    @ApiModelProperty(value = "办公电话")
    private String officePhone;
    @ApiModelProperty(value = "开户银行")
    private String bankName;
    @ApiModelProperty(value = "银行账号")
    private String bankNumber;
}
