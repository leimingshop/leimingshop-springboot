/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Data
@ApiModel(value = "订单发票表")
public class OrderInvoiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单SN")
    private Long orderSn;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty("店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;
    @ApiModelProperty(value = "下单时间")
    private Date createOrderDate;
    @ApiModelProperty(value = "申请时间")
    private Date applyDate;
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
    @ApiModelProperty(value = "开票进度（1：待开票、2：已开票、3：待换开、4：已换开）")
    private Integer invoiceEvolve;
    @ApiModelProperty(value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:已完成")
    private Integer orderStatus;
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