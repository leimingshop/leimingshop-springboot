/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.menu;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 店铺菜单表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_menu")
public class StoreMenuEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径URL
     */
    private String menuUrl;

    /**
     * 父级菜单ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单权限标识
     */
    private String menuPermission;

    /**
     * icon图标
     */
    private String menuIcon;

    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer menuType;

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