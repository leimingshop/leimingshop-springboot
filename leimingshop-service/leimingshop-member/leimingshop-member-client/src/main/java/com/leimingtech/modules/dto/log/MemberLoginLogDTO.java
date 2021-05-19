/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.log;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户登录日志表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-30
 */
@Data
@ApiModel(description = "MemberLoginLogDTO")
public class MemberLoginLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String loginName;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    /**
     * 操作ip
     */
    @ApiModelProperty(value = "操作ip")
    private String ip;

    /**
     * 用户代理
     */
    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    /**
     * 登录地区
     */
    @ApiModelProperty(value = "登录地区")
    private String loginArea;

    /**
     * 登录方式 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序
     */
    @ApiModelProperty(value = "登录方式 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序")
    private Integer source;

    /**
     * 手机号
     */
    @Length(max = 11, message = "手机号只能为11位", groups = AddGroup.class)
    @Length(max = 11, message = "手机号只能为11位", groups = UpdateGroup.class)
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    /**
     * 登录状态   0：登录成功   1：登陆失败
     */
    @ApiModelProperty(value = "登录状态   0：登录成功   1：登陆失败")
    private Integer status;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date createDate;

}