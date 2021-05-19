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
 * @Author: SWH ab4856812@163.com
 * @Description:售后退款日志
 * @Date: 2019/6/24 9:16
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_aftersale_refund_records")
public class AftersaleRefundRecordsEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 售后单号
     */
    private Long aftersaleSn;
    /**
     * 退款状态(1:待审核,2:待退款,3:退款中,4:退款成功,5:退款失败)
     */
    private Integer refundStatus;

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