/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constant;

/**
 * 功能描述：
 * <redis配置常量类>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2020/3/9 9:45
 **/
public class ActivityRedisConstants {

    /**
     * 秒杀场次
     */
    public static final String SECKILL_SESSION = "seckill_session:";
    /**
     * 秒杀商品剩余库存
     */
    public static final String SECKILL_GOODS_SURPLUS_STORAGE = "seckill_goods_surplus_storage:";
    /**
     * 秒杀商品价格
     */
    public static final String SECKILL_GOODS_PRICE = "seckill_goods_price:";
    /**
     * 拼团商品剩余库存
     */
    public static final String GROUP_GOODS_SURPLUS_STORAGE = "group_goods_surplus_storage:";
    /**
     * 拼团商品价格
     */
    public static final String GROUP_GOODS_PRICE = "group_goods_price:";
    /**
     * 限时购商品剩余库存
     */
    public static final String FLASH_GOODS_SURPLUS_STORAGE = "flash_sale_goods_surplus_storage:";
    /**
     * 限时购商品价格
     */
    public static final String FLASH_GOODS_PRICE = "flash_sale_goods_price:";
    /**
     * 缓存默认时间 1天
     */
    public static final int JEDIS_EXPIRE = 60 * 60 * 24;

    private ActivityRedisConstants() {
    }

}
