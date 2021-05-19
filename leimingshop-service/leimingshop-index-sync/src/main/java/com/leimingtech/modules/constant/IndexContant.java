/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constant;


/**
 * 索引常量类
 *
 * @author lixiang
 * @date 2019/7/23 21:16
 **/
public interface IndexContant {

    /**
     * 商品索引名称
     */
    String GOODS_INDEX_NAME = "goods";

    /**
     * 商品规格索引名称
     */
    String GOODS_SPEC_INDEX_NAME = "goods_spec";

    /**
     * 商品搜索关键字索引名称
     */
    String SEARCH_KEYWORD_INDEX_NAME = "search_keyword";

    /**
     * 索引锁前缀
     */
    String REDIS_PREFIX = "index_sync:";

    /**
     * 商品索引同步锁
     */
    String GOODS_INDEX_SYNC_LOCK = "goods_index_sync_lock";

    /**
     * 商品规格索引同步锁
     */
    String GOODS_SPEC_INDEX_SYNC_LOCK = "goods_index_sync_lock";

    /**
     * 商品索引上次同步时间 key name
     */
    String GOODS_INDEX_LAST_SYNC_TIME = "goods_index_last_sync_time";

    /**
     * 规格索引上次同步时间 key name
     */
    String GOODS_SPEC_INDEX_LAST_SYNC_TIME = "goods_spec_index_last_sync_time";

    /**
     * 优惠券索引名称 类型
     */
    String COUPONS_ACTIVITY_NAME = "coupons_activity";
    String COUPONS_ACTIVITY_TYPE = "coupons_activity";

    /**
     * 会员优惠券索引名称 类型
     */
    String MEMBER_COUPONS_NAME = "member_coupons";
    String MEMBER_COUPONS_TYPE = "member_coupons";

    /**
     * 满减活动索引名称 类型
     */
    String REDUCE_ACTIVITY_NAME = "reduce_activity";
    String REDUCE_ACTIVITY_TYPE = "reduce_activity";

    /**
     * 店铺索引名称
     */
    String STORE_INDEX_NAME = "store";

    /**
     * 店铺索引同步锁
     */
    String STORE_INDEX_SYNC_LOCK = "store_index_sync_lock";


    /**
     * 店铺索引上次同步时间 key name
     */
    String STORE_INDEX_LAST_SYNC_TIME = "store_index_last_sync_time";

    /**
     * 定时更新商品索引
     */
    String TIME_GOODS_INDEX = "time";
}
