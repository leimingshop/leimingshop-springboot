/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goods.spec;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品属性
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_attribute")
public class GoodsAttributeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 属性名称
     */
    private String attrName;

    /**
     * 属性值（多个属性值之间用逗号隔开）
     */
    private String attrValue;

    /**
     * 属性ID
     */
    private Long attributeId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 展示类型（默认0单选，1下拉框，2多选）
     */
    private Integer showType;

    /**
     * 是否索引（预留字段）
     */
    private Integer isKey;

    /**
     * 排序
     */
    private Integer sort;

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