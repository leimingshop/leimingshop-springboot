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
 * 商品分类表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_goods_class")
public class GoodsClassEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    private String gcName;

    /**
     * 父ID
     */
    private Long gcParentId;

    /**
     * 排序
     */
    private Integer gcSort;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 层级path
     */
    private String gcIdpath;

    /**
     * 分类类型（0:实体商品分类，1:虚拟商品分类）
     */
    private Integer classType;
    /**
     * 店铺ID
     */
    private Long storeId;

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
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}