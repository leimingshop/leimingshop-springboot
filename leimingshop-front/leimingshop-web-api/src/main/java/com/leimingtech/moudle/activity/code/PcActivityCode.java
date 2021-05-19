/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.code;

/**
 * 活动入参日志码
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface PcActivityCode {

    /**
     * 首页-秒杀专场访问成功
     */
    String HOME_SECKILL_CODE = "200350";
    String HOME_SECKILL_DESC = "访问首页秒杀专场";


    /**
     * 首页-访问优惠券中心成功
     */
    String HOME_COUPON_CENTER_CODE = "200351";
    String HOME_COUPON_CENTER_DESC = "访问优惠券中心";

    /**
     * 领劵中心-领取优惠券成功
     */
    String HOME_RECEIVE_COUPON_CODE = "200352";
    String HOME_RECEIVE_COUPON_DESC = "领取优惠券";


    /**
     * 秒杀专区-访问秒杀专区
     */
    String HOME_SECKILL_AREA_CODE = "200353";
    String HOME_SECKILL_AREA_DESC = "访问秒杀专区";

    /**
     * 秒杀专区-设置描述提醒
     */
    String HOME_SECKILL_REMIND_CODE = "200354";
    String HOME_SECKILL_REMIND_DESC = "设置秒杀提醒";


    /**
     * 秒杀专区-正在秒杀商品列表
     */
    String HOME_SECKILL_CURRENT_GOODS_CODE = "200355";
    String HOME_SECKILL_CURRENT_GOODS_DESC = "秒杀专区-正在秒杀商品列表访问成功";


    /**
     * 商品详情页-优惠券列表
     */
    String GOODS_DETAIL_COUPON_LIST_CODE = "200356";
    String GOODS_DETAIL_COUPON_LIST_DESC = "访问商品详情页-优惠券列表";


    /**
     * 个人中心-优惠券列表访问成功
     */
    String CENTER_COUPON_LIST_CODE = "200357";
    String CENTER_COUPON_LIST_DESC = "访问个人中心-优惠券列表";

}
