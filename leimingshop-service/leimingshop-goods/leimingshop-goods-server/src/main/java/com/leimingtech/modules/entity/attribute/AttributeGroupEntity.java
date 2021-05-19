/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.attribute;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 属性分组表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_attribute_group")
public class AttributeGroupEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 属性分组名称
     */
    private String groupName;

    /**
     * 规格数量
     */
    private Integer lableNum;

    /**
     * 属性分组排序
     */
    private Integer sort;

    /**
     * 属性分组状态（默认1:启用,2:禁用）
     */
    private Integer groupStatus;

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
