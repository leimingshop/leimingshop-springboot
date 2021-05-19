/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 售后申请表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_aftersale_apply")
public class AftersaleApplyEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 售后单号
     */
    private Long aftersaleSn;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private Long orderSn;

    /**
     * 订单项ID
     */
    private Long orderGoodsId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 用户ID
     */
    private Long memberId;

    /**
     * 用户昵称
     */
    private String memberName;

    /**
     * 售后方式（0:退货,1:换货,2:仅退款）
     */
    private Integer aftersaleType;

    /**
     * 售后数量
     */
    private Integer applyNum;

    /**
     * 申请售后原因ID
     */
    private Long aftersaleReasonId;

    /**
     * 申请售后原因
     */
    private String aftersaleReason;

    /**
     * 问题描述
     */
    private String aftersaleExplain;

    /**
     * 凭证图片
     */
    private String aftersalePics;

    /**
     * 审核状态（1:商家审核中,2:平台审核中,3:已完成,4:已取消）
     */
    private Integer auditStatus;

    /**
     * 审核结果（0:未审核,1:审核通过,2:审核不通过,3:审核中,4:用户取消）
     */
    private Integer auditResult;

    /**
     * 收货人姓名
     */
    private String receiver;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货人地址
     */
    private String receiverAddress;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系人电话
     */
    private String contactsPhone;

    /**
     * 是否开发票（0:否,1:是）
     */
    private Integer isInvoice;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

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
     * 删除标记（默认0:未删除,1:已删除）
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
