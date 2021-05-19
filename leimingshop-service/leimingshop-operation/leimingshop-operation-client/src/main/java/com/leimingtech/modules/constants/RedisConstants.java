/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constants;

/**
 * @Description redis常量设置
 * @Author 刘远杰
 * @Date 2019/5/15 16:05
 * Version 7.0
 **/
public class RedisConstants {

    /**
     * 广告缓存
     */
    public static final String ADV_PREFIX = "adv:";
    /**
     * 楼层广告缓存
     */
    public static final String ADV_FLOOR_PREFIX = "advFloor:";
    /**
     * 分类广告缓存
     */
    public static final String ADV_CLASS_PREFIX = "advClass:";
    /**
     * 专题页缓存
     */
    public static final String TOPIC_PREFIX = "topic:";
    /**
     * h5楼层缓存
     */
    public static final String WEB_FLOOR_PREFIX = "webFloor:";
    /**
     * h5店铺楼层缓存
     */
    public static final String STORE_WEB_FLOOR_PREFIX = "StoreWebFloor:";
    /**
     * 移动首页菜单
     */
    public static final String MOBILE_INDEX_MENU_PREFIX = "mobileIndexMenu:";
    /**
     * 推荐商品
     */
    public static final String RECOMMEND_GOODS = "recommendgoods:";
    /**
     * 店铺商品分类缓存
     */
    public static final String STORE_GOODS_CLASS = "storeGoodsClass:";
    /**
     * 缓存默认时间
     */
    public static final int JEDIS_EXPIRE = 600;

    private RedisConstants() {
    }

}
