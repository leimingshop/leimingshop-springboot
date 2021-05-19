/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.weibo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangtai
 * @date 2020/4/10 10:16
 * @Description:
 */
@Data
@ApiModel("微博登录认证返回实体")
public class WeiboAccessTokenDTO implements Serializable {


    @ApiModelProperty("用户授权的唯一票据，用于调用微博的开放接口，同时也是第三方应用验证微博用户登录的唯一票据")
    private String accessToken;

    @ApiModelProperty("授权用户的UID")
    private String uid;

    @ApiModelProperty("access_token的生命周期，单位是秒数。")
    private String expiresIn;
}
