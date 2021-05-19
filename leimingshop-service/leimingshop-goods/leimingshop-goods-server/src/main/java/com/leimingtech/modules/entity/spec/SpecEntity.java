/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.spec;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 规格实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_spec")
public class SpecEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 0:text; 1:image
     */
    private Integer specFormat;

    /**
     * 规格值列（所有规格值组合，中间用“,”隔开）
     */
    private String specValue;

    /**
     * 排序
     */
    private Integer spSort;

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
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
