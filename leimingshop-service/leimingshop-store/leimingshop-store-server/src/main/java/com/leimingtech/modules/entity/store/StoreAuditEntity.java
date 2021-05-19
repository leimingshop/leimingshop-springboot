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
@TableName("lmshop_store_audit")
public class StoreAuditEntity extends BaseEntity {
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
     * 店铺简介
     */
    private String storeIntro;
    /**
     * 店铺logo
     */
    private String storeLogo;
    /**
     * 店铺类型（1:自营商户，2:普通商户）
     */
    private Integer storeType;
    /**
     * 店铺联系人电话
     */
    private String linkmanPhone;
    /**
     * 店铺联系人
     */
    private String storeLinkman;
    /**
     * 普通信息审核状态(10 待审核 20 审核通过 30 审核拒绝)
     */
    private Integer infoAuditStatus;
    /**
     * 普通信息审核备注
     */
    private String infoAuditCause;
}