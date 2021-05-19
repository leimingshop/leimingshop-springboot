/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto;

import lombok.Data;

/**
 * 微信用户信息
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
@Data
public class WxUserDto {

    private Boolean subscribe;
    private String openId;
    private String nickname;
    /**
     * 性别描述信息：男、女、未知等.
     */
    private String sexDesc;
    /**
     * 性别表示：1，2等数字.
     */
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headImgUrl;
    private Long subscribeTime;
    /**
     * https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&announce_id=11513156443eZYea&version=&lang=zh_CN
     * <pre>
     * 只有在将公众号绑定到微信开放平台帐号后，才会出现该字段。
     * 另外，在用户未关注公众号时，将不返回用户unionID信息。
     * 已关注的用户，开发者可使用“获取用户基本信息接口”获取unionID；
     * 未关注用户，开发者可使用“微信授权登录接口”并将scope参数设置为snsapi_userinfo，获取用户unionID
     * </pre>
     */
    private String unionId;
    private String remark;
    private Integer groupId;
    private Long[] tagIds;

    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）.
     */
    private String[] privileges;

    /**
     * subscribe_scene 返回用户关注的渠道来源.
     * ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
     */
    private String subscribeScene;

    /**
     * qr_scene 二维码扫码场景（开发者自定义）.
     */
    private String qrScene;

    /**
     * qr_scene_str 二维码扫码场景描述（开发者自定义）.
     */
    private String qrSceneStr;
}
