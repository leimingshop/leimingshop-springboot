/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.goodsclass;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品分类和属性关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_class_attr")
public class GoodsClassAttrEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品分类ID
     */
    private Long gcClassId;

    /**
     * 属性ID
     */
    private Long attrId;

    /**
     * 属性名称
     */
    private String attrName;

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