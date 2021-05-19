/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺发票设置表
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_invoice")
public class StoreInvoiceEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 是否允许开具发票（0：不允许，1：允许）
     */
    private Integer invoiceFlag;
    /**
     * 发票类型(多选 1:电子、2：纸质、3：增值税普通发票)
     */
    private String invoiceType;
    /**
     * 发票内容（多选 1：商品明细、2：商品类别）
     */
    private String invoiceContent;
    /**
     * 收件人
     */
    private String addressee;
    /**
     * 收件人电话
     */
    private String addresseePhone;
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
     * 邮寄地址（空格分隔）
     */
    private String mailingAddress;
    /**
     * 详细地址
     */
    private String detailedAddress;
}