/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "H5发票详情")
public class H5InvoiceDetailDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "申请时间")
    private Date applyDate;
    @ApiModelProperty(value = "下单时间")
    private Date createOrderDate;
    @ApiModelProperty(value = "用户提交抬头类型（默认0：不开票、1：个人、2：单位）")
    private Integer companyType;
    @ApiModelProperty(value = "用户提交发票类型(1：电子、2：纸质、3：增值税)")
    private Integer memberInvoiceType;
    @ApiModelProperty(value = "用户提交发票内容（1：商品明细、2：商品类别）")
    private Integer memberInvoiceContent;
    @ApiModelProperty(value = "商家开具发票类型(1：电子、2：纸质、3：增值税)")
    private Integer storeInvoiceType;
    @ApiModelProperty(value = "商家开具发票内容（1：商品明细、2：商品类别）")
    private Integer storeInvoiceContent;
    @ApiModelProperty(value = "个人名称（个人发票使用）")
    private String personalName;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
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
    @ApiModelProperty(value = "开票进度（1：待开票、2：已开票、3：待换开、4：已换开）")
    private Integer invoiceEvolve;
    @ApiModelProperty(value = "是否换开（默认0：未换开、1：换开）")
    private Integer changeFlag;
    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;
    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;
    @ApiModelProperty(value = "开票时间")
    private Date invoiceDate;
    @ApiModelProperty(value = "物流公司")
    private String logisticsCompanies;
    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;
    @ApiModelProperty(value = "发票文件地址（url）")
    private String fileUrl;
}
