/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.auth;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 店铺认证信息表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_auth")
public class StoreAuthEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;

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
    private String companyAddress;

    /**
     * 公司详细地址
     */
    private String companyAddressDetail;

    /**
     * 公司所属省份
     */
    private Long companyProvinceId;

    /**
     * 公司所属市
     */
    private Long companyCityId;

    /**
     * 公司所区县
     */
    private Long companyAreaId;

    /**
     * 公司电话
     */
    private String companyPhone;

    /**
     * 注册资金
     */
    private Integer companyRegisteredCapital;

    /**
     * 纳税人识别号
     */
    private String taxpayerId;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 银行开户名称
     */
    private String bankAccountName;

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
     * 身份证，正面
     */
    private String idCardPeoPicture;
    /**
     * 身份证，反面
     */
    private String idCardNatPicture;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}