/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.loginlog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @Author: weixianchun
 * @Description: 店铺登录日志
 * @Date :2019/6/28 10:24
 * @Version V1.0
 **/
@Data
@ApiModel(description = "StoreLoginLogDTO")
public class StoreLoginLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "storeId")
    private Long storeId;
    @ApiModelProperty(value = "用户操作   0：用户登录   1：用户退出")
    private Integer operation;
    @ApiModelProperty(value = "状态  0：失败    1：成功    2：账号已锁定")
    private Integer status;
    @ApiModelProperty(value = "用户代理")
    private String userAgent;
    @ApiModelProperty(value = "操作IP")
    private String ip;
    @ApiModelProperty(value = "用户名")
    private String creator;
    @ApiModelProperty(value = "操作时间")
    private Date createDate;
}