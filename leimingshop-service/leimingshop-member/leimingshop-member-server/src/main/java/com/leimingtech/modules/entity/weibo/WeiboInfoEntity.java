/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.weibo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangtai
 * @date 2020/4/10 10:53
 * @Description:
 */
@Data
@ApiModel("微博登录返回数据")
public class WeiboInfoEntity {

    @ApiModelProperty("用户UID")
    private String uid;

    @ApiModelProperty("用户昵称")
    private String nicname;

    @ApiModelProperty("性别，1：男、2：女、0：未知")
    private String sex;

    @ApiModelProperty("用户所在城市ID")
    private String city;

    @ApiModelProperty("用户所在省级ID")
    private String province;

    @ApiModelProperty("用户所在地")
    private String location;

    @ApiModelProperty("用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语")
    private String language;

    @ApiModelProperty("用户头像地址（中图），50×50像素")
    private String headimgurl;


}
