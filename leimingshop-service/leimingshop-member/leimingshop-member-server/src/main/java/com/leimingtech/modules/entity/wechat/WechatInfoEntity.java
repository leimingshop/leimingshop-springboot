/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.wechat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName WechatInfoEntity
 * @Description 微信用户信息
 * @Author DY
 * @Date 2019/5/24 9:50
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatInfoEntity {

    /**
     * openId
     */
    private String openId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 国家
     */
    private String country;

    /**
     * 头像
     */
    private String headimgUrl;

    /**
     * 特权
     */
    private String privilege;

    /**
     * unionid
     */
    private String unionid;

}
