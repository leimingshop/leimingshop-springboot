/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.point;


/**
 * 用户积分枚举
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-07-20 15:49
 **/
public enum MemberPointLogEnum {

    /**
     * 类型 1积分 2成长值
     */
    POINT_TYPE(1, null),
    GROWTH_TYPE(2, null),

    /**
     * 积分/成长值获取类型（1:新手欢迎奖励;2:设置头像;3:设置昵称;4:完善个人信息;
     * 5:首次关注店铺;6:首次分享商品;7:首次收藏商品;8:首次购物成功;9:首次完成评价;
     * 10:每日登录;11:每日签到;12:分享商品;13:邀请好友;14:好友首次下单成功;15:评价;
     * 16:更多规则购物消费;17:更多规则商品评价;18:退货进行积分成长值扣减
     */

    WELCOME_SOURCE_TYPE(1, "新手欢迎奖励"),
    AVATAR_SOURCE_TYPE(2, "设置头像"),
    NICKNAME_SOURCE_TYPE(3, "设置昵称"),
    MEMBER_INFORMATION_SOURCE_TYPE(4, "完善个人信息"),
    ATTENTION_STORE_SOURCE_TYPE(5, "首次关注店铺"),
    FIRST_SHARE_GOODS_SOURCE_TYPE(6, "首次分享商品"),
    FAVORITES_GOODS_SOURCE_TYPE(7, "首次收藏商品"),
    FIRST_ORDER_SOURCE_TYPE(8, "首次购物成功"),
    FIRST_EVALUATE_ORDER_SOURCE_TYPE(9, "首次完成评价"),
    LOGIN_SOURCE_TYPE(10, "每日登录"),
    SIGNIN_SOURCE_TYPE(11, "每日签到"),
    SHARE_GOODS_SOURCE_TYPE(12, "分享商品"),
    INVITE_FRIENDS_SOURCE_TYPE(13, "邀请好友"),
    FRIEND_ORDER_SOURCE_TYPE(14, "好友首次下单成功"),
    EVALUATE_ORDER_SOURCE_TYPE(15, "评价"),
    MORE_RULES_SHOPPING(16, "更多规则购物消费"),
    MORE_RULES_EVALUATE(17, "更多规则商品评价"),
    REFUND_ORDER(18, "售后进行积分成长值扣减"),

    /**
     * 是否增加积分/成长值 0全部增加 1只增加积分 2只增加成长值
     */
    INSERT_ALL(0, null),
    INSERT_POINT(1, null),
    INSERT_GROWTH(2, null);

    private int code;

    private String value;

    MemberPointLogEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return this.code;
    }

    public String value() {
        return this.value;
    }

}
