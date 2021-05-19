/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.log;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 操作日志
 *
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLogOperation extends BaseLog {
    private static final long serialVersionUID = 1L;
    /**
     * 模块名称，如：sys
     */
    private String module;
    /**
     * 用户操作
     */
    private String operation;
    /**
     * 请求URI
     */
    private String requestUri;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 请求时长(毫秒)
     */
    private Integer requestTime;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 状态  0：失败   1：成功
     */
    private Integer status;

    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间时间
     */
    private Date updateDate;


}