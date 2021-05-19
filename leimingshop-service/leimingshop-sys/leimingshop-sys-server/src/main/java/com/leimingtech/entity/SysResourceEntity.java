/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 资源管理
 *
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_resource")
public class SysResourceEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源编码，如菜单ID
     */
    private String resourceCode;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源URL
     */
    private String resourceUrl;
    /**
     * 请求方式（如：GET、POST、PUT、DELETE）
     */
    private String resourceMethod;
    /**
     * 菜单标识  0：非菜单资源   1：菜单资源
     */
    private Integer menuFlag;
    /**
     * 认证等级   0：权限认证   1：登录认证    2：无需认证
     */
    private Integer authLevel;

}
