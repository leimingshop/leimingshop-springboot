/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 *
 * @since 1.0.0
 */
@Data
@ApiModel(description = "SysLogOperationDTO")
public class SysLogOperationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "模块名称，如：sys")
    private String module;

    @ApiModelProperty(value = "用户操作")
    private String operation;

    @ApiModelProperty(value = "请求URI")
    private String requestUri;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "请求时长(毫秒)")
    private Integer requestTime;

    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    @ApiModelProperty(value = "操作IP")
    private String ip;

    @ApiModelProperty(value = "状态  0：失败   1：成功")
    private Integer status;

    @ApiModelProperty(value = "用户名")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}