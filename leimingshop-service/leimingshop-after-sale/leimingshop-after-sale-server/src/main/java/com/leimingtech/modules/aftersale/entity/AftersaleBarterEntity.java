/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单换货表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_aftersale_barter")
public class AftersaleBarterEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 售后单号
     */
    private Long aftersaleSn;

    /**
     * 服务单号
     */
    private Long serviceSn;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private Long orderSn;

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
     * 用户名称
     */
    private String memberName;

    /**
     * 售后物流状态（1为买家待发货,2为卖家待收货,3卖家已收货,4为买家待收货,5为已完成）
     */
    private Integer logisticsStatus;

    /**
     * 售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:退款中,5:退款成功,6:换货失败,7:待换货入库,8:换货出库中,9:换货成功）
     */
    private Integer aftersaleStatus;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactsPhone;

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
     * 买家发货时间
     */
    private Date buyerDeliveryTime;

    /**
     * 买家发货物流公司
     */
    private String buyerLogisticsCompany;

    /**
     * 买家发货物流单号
     */
    private String buyerLogisticsNumber;

    /**
     * 买家发货联系电话
     */
    private String buyerLogisticsContactsPhone;

    /**
     * 商家发货单号
     */
    private Long sellerDeliveryNo;

    /**
     * 商家发货时间
     */
    private Date sellerDeliveryTime;

    /**
     * 商家发货物流公司
     */
    private String sellerLogisticsCompany;

    /**
     * 商家发货物流单号
     */
    private String sellerLogisticsNumber;

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