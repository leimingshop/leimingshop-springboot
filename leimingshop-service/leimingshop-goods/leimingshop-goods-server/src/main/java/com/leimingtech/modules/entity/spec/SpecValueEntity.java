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
 * 规格值实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_spec_value")
public class SpecValueEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 所属规格id
     */
    private Long specId;

    /**
     * 规格值名称
     */
    private String specValueName;

    /**
     * 规格图片
     */
    private String specValueImage;

    /**
     * 规格值排序
     */
    private Integer specValueSort;

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
