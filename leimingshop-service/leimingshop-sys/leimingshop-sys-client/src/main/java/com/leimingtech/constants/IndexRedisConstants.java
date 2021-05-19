/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.constants;

/**
 * @author xuzhch
 * @className IndexRedisConstants
 * @description 首页缓存信息
 * @date 2020/3/19/019
 */
public class IndexRedisConstants {
    /**
     * 首页商户销量缓存
     */
    public static final String INDEX_STORE_SELL = "index:store:sell:";
    /**
     * 首页商户销量缓存
     */
    public static final String INDEX_GOODS_SELL = "index:goods:sell:";
    public static final String INDEX_BASE = "index:base:";
    /**
     * 缓存默认时间
     */
    public static final int JEDIS_EXPIRE = 7200;

    private IndexRedisConstants() {
    }
}
