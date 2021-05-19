/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.security.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 资源
 *
 * @since 1.0.0
 */
@Data
public class ResourceBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源URL
     */
    private String resourceUrl;
    /**
     * 请求方式（如：GET、POST、PUT、DELETE）
     */
    private String resourceMethod;
    /**
     * 认证等级   0：权限认证   1：登录认证    2：无需认证
     */
    private Integer authLevel;

}
