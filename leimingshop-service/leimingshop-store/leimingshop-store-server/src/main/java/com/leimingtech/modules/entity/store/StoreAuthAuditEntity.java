/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺审核表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_auth_audit")
public class StoreAuthAuditEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 法人名称
     */
    private String legalPersonName;
    /**
     * 营业执照电子版
     */
    private String electronicBusinessLicense;
    /**
     * 公司地址
     */
    private String companyAddressDetail;
    /**
     * 纳税人识别号
     */
    private String taxpayerId;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 开户行账号
     */
    private String bankAccountNumber;
    /**
     * 纳税人资格证
     */
    private String taxpayerPicture;
    /**
     * 银行开户许可证
     */
    private String bankAccountPicture;
    /**
     * 身份证反面
     */
    private String idCardNatPicture;
    /**
     * 身份证正面
     */
    private String idCardPeoPicture;
    /**
     * 公司信息审核状态(10 待审核 20 审核通过 30 审核拒绝)
     */
    private Integer authAuditStatus;
    /**
     * 公司审核备注
     */
    private String authAuditCause;
    /**
     * 公司电话
     */
    private String companyPhone;
}