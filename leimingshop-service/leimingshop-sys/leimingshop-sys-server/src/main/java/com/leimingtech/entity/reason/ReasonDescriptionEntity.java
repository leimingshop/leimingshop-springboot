/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.reason;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 原因描述
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_reason_description")
public class ReasonDescriptionEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 类型（0：退货，1：换货，2：申请退款 ，3：取消订单）
     */
    private String type;

    /**
     * 适用角色（0：会员，1：商家，2：平台）
     */
    private String role;

    /**
     * 禁止操作（默认为0，为1的时候不可删除、编辑）
     */
    private Integer forbidOperation;

    /**
     * 描述内容
     */
    private String content;

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
