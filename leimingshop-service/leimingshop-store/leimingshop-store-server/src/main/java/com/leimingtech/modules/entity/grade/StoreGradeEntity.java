/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.grade;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺等级表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_grade")
public class StoreGradeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 等级名称
     */
    private String sgName;

    /**
     * 等级分值
     */
    private Integer sgGradeScore;

    /**
     * 允许发布的商品数量(默认为0)
     */
    private Integer sgGoodsLimit;

    /**
     * 收费标准
     */
    private BigDecimal sgPrice;

    /**
     * 佣金比例
     */
    private Double brokerageScale;

    /**
     * 是否启用（0禁用，默认为1启用）
     */
    private Integer showFlag;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 更新者
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