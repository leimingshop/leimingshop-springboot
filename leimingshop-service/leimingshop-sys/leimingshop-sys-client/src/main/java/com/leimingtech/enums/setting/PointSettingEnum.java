/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums.setting;

/**
 * 积分奖励设置枚举
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/23 14:13
 **/
public enum PointSettingEnum {

    /**
     * 奖励设置数据库key
     */
    REWARD_SETTING_NAME("reward"),

    /**
     * 更多规则数据库 key
     */
    MORE_RULE_NAME("morerule"),

    /**
     * 使用设置 数据库Key
     */
    USER_SETTING_NAME("user"),

    /**
     * 新手任务-新手欢迎奖励 key
     */
    REWARD_WELCOME("welcome"),

    /**
     * 新手任务-设置头像 key
     */
    REWARD_SETTING_AVATAR("avatar"),

    /**
     * 新手任务-设置昵称 key
     */
    REWARD_SETTING_NIKENAME("nikename"),

    /**
     * 新手任务-完善个人信息 key
     */
    REWARD_INFORMATION("information"),

    /**
     * 新手任务-首次关注店铺 key
     */
    REWARD_ATTENTION_STORE("attentionStore"),

    /**
     * 新手任务-首次分享商品 key
     */
    REWARD_SHARE_GOODS("sharegoods"),

    /**
     * 新手任务-首次收藏商品 key
     */
    REWARD_FAVORITES_GOODS("favoritesgoods"),

    /**
     * 新手任务-首次购物成功 key
     */
    REWARD_FIRST_ORDER("firstOrder"),

    /**
     * 新手任务-首次完成评价 key
     */
    REWARD_EVALUATE_ORDER("evaluateOrder"),

    /**
     * 日常任务-每日登录 key
     */
    DAILY_LOGIN("login"),

    /**
     * 日常任务-每日签到 key
     */
    DAILY_SIGNIN("signin"),

    /**
     * 日常任务-分享商品 key
     */
    DAILY_SHARE_GOODS("shareGoods"),

    /**
     * 日常任务-邀请好友 key
     */
    DAILY_INVITE_FRIENDS("inviteFriends"),

    /**
     * 日常任务-好友首次下单成功 key
     */
    DAILY_FRIEND_ORDER("friendOrder"),

    /**
     * 成长值 key
     */
    GROWTH_VALUE_KEY("growthValue"),

    /**
     * 成长值 key
     */
    POINT_VALUE_KEY("pointValue"),

    /**
     * 日常任务-最多奖励次数 key
     */
    FREQUENCY_KEY("frequency");


    private String value;

    PointSettingEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

