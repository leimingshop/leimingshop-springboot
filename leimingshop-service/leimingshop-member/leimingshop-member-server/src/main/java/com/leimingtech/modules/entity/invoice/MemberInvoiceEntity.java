/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.invoice;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_member_invoice")
public class MemberInvoiceEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long memberId;
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
     * 是否默认（默认0：不默认、1：默认）
     */
    private Integer defaultFlag;
    /**
     * 增值税抬头标记（默认：0非增值税抬头、1：增值税发票抬头）
     */
    private Integer vatFlag;
}