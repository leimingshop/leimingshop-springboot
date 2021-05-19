/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.store;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store")
public class StoreEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺认证（默认0未认证，1已认证）
     */
    private Integer storeAuth;

    /**
     * 店铺等级ID
     */
    private Long gradeId;

    /**
     * 地区id
     */
    private Long areaId;

    /**
     * 店铺所在地（省）
     */
    private Long provinceId;

    /**
     * 店铺坐在市id
     */
    private Long cityId;

    /**
     * 地区内容（3级地址组合）
     */
    private String areaInfo;

    /**
     * 地址（详细地址）
     */
    private String storeAddress;
    /**
     * 店铺二维码
     */
    private String qrCode;

    /**
     * 启用状态 0 启用 1 禁用
     */
    private Integer isEnable;

    /**
     * 店铺logo
     */
    private String storeLogo;
    /**
     * 店铺简介
     */
    private String storeIntro;

    /**
     * 店铺类型（1:自营商户，2:普通商户）
     */
    private Integer storeType;
    /**
     * 店铺联系人
     */
    private String storeLinkman;
    /**
     * 联系人手机号
     */
    private String linkmanPhone;
    /**
     * 审核状态 10 待审核 20 审核通过 30 审核拒绝
     */
    private Integer registerAuditStatus;
    /**
     * 审核原因
     */
    private String registerAuditCause;

    /**
     * 物流评价
     */
    private Double logisticsEvaluate;

    /**
     * 商品评价
     */
    private Double goodsEvaluate;

    /**
     * 售后评价
     */
    private Double afterSaleEvaluate;

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