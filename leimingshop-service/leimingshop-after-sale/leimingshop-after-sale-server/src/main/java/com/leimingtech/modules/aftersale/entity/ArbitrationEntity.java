/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 仲裁表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_arbitration")
public class ArbitrationEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 售后单号 唯一
     */
    private Long aftersaleSn;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 会员账号
     */
    private String memberName;
    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系方式
     */
    private String contactsWay;
    /**
     * 详细说明
     */
    private String detailedDescription;
    /**
     * 图片数组（,分隔）
     */
    private String imagesArr;
    /**
     * 仲裁申请时间
     */
    private Date arbitrationApplyDate;
    /**
     * 仲裁审核时间
     */
    private Date arbitrationAuditDate;
    /**
     * 仲裁状态（1:审核通过、2:审核不通过、默认：3:待审核）
     */
    private Integer arbitrationStatus;
    /**
     * 平台审核理由
     */
    private String auditReason;
    /**
     * 平台审核理由
     */
    private String remark;
    /**
     * 审核人
     */
    private String auditor;
    /**
     * 仲裁类型（0：售后-退货、1：售后-换货）
     */
    private Integer arbitrationType;
}
