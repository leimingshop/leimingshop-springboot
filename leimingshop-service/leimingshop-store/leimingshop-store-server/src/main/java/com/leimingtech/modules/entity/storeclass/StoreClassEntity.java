/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.storeclass;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 店铺分类表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_class")
public class StoreClassEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 分类ID
     */
    private Long classId;

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