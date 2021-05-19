/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 菜单管理
 *
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
public class SysMenuEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 上级ID，一级菜单为0
     */
    private Long pid;
    /**
     * 菜单名称
     */
    @TableField(exist = false)
    private String name;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer type;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 权限标识，如：sys:menu:save
     */
    private String permissions;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 上级菜单名称
     */
    @TableField(exist = false)
    private String parentName;

}
