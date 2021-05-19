/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.invoice;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_invoice")
public class OrderInvoiceEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单SN
     */
    private Long orderSn;
    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 用户名
     */
    private String memberName;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 下单时间
     */
    private Date createOrderDate;
    /**
     * 申请时间
     */
    private Date applyDate;

    /**
     * 用户提交抬头类型（默认0：不开票、1：个人、2：单位）
     */
    private Integer companyType;
    /**
     * 用户提交发票类型(1：电子、2：纸质、3：增值税)
     */
    private Integer memberInvoiceType;
    /**
     * 用户提交发票内容（1：商品明细、2：商品类别）
     */
    private Integer memberInvoiceContent;
    /**
     * 商家开具发票类型(1：电子、2：纸质、3：增值税)
     */
    private Integer storeInvoiceType;
    /**
     * 商家开具发票内容（1：商品明细、2：商品类别）
     */
    private Integer storeInvoiceContent;
    /**
     * 个人名称（个人发票使用）
     */
    private String personalName;
    /**
     * 收票人名称
     */
    private String addresseeName;
    /**
     * 收票人手机
     */
    private String addresseePhone;
    /**
     * 收票人邮箱
     */
    private String addresseeMail;
    /**
     * 收票人地址（空格分隔）
     */
    private String addresseeAddress;
    /**
     * 收票人详细地址
     */
    private String detailedAddress;
    /**
     * 省ID
     */
    private Long provinceId;
    /**
     * 市ID
     */
    private Long cityId;
    /**
     * 区ID
     */
    private Long districtId;
    /**
     * 街道ID
     */
    private Long streetId;
    /**
     * 企业名称（公司抬头）
     */
    private String company;
    /**
     * 企业税号
     */
    private String dutyParagraph;
    /**
     * 注册地址
     */
    private String registeredAddress;
    /**
     * 办公电话
     */
    private String officePhone;
    /**
     * 开户银行
     */
    private String bankName;
    /**
     * 银行账号
     */
    private String bankNumber;
    /**
     * 开票进度（1：待开票、2：已开票、3：待换开、4：已换开）
     */
    private Integer invoiceEvolve;
    /**
     * 是否换开（默认0：未换开、1：换开）
     */
    private Integer changeFlag;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNumber;
    /**
     * 开票时间
     */
    private Date invoiceDate;
    /**
     * 物流公司
     */
    private String logisticsCompanies;
    /**
     * 物流单号
     */
    private String logisticsNumber;
    /**
     * 发票文件地址（url）
     */
    private String fileUrl;
}