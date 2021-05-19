/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.payment;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 支付方式PaymentEntity
 * @Date :2019/5/20 14:52
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_payment")
public class PaymentEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 支付标识
     */
    private String paymentCode;

    /**
     * 支付名称
     */
    private String paymentName;

    /**
     * 支付对接配置信息（json格式）
     */
    private String paymentConfig;

    /**
     * 状态（默认0禁用，1启用）
     */
    private Integer paymentState;

    /**
     * 支付图标
     */
    private String paymentLogo;

    /**
     * 说明
     */
    private String remark;

    /**
     * 客户端类型
     */
    private String clientType;

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
