/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 操作日志
 *
 * @since 1.0.0
 */
@Data
@TableName("sys_log_operation")
public class SysLogOperationEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId
    private Long id;
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
}