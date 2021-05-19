/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.wechat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 功能描述：
 * 微信app登录
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/4/20
 **/

@Data
@ToString
@ApiModel(description = "WechatAppLoginDTO")
public class WechatAppLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("验证码")
    private String validCode;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("0：男 1：女")
    private String sex;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("授权用户唯一标识")
    private String openId;

    @ApiModelProperty("当且仅当网站已获得用户的userinfo授权时，才会出现该字段")
    private String unionId;
}
