/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constant.order;

/**
 * 功能描述：
 * <redis配置常量类>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/20 9:45
 **/
public class RedisConstants {

    /**
     * 订单保存状态
     */
    public static final String SAVE_ORDER_PREFIX = "save_order";
    /**
     * 运费规则
     */
    public static final String FREIGHT_RULE = "freight_rule:";
    /**
     * 保存成功
     */
    public static final int SUSSESS = 1;
    /**
     * 保存失败
     */
    public static final int FAIL = 2;
    /**
     * 保存中
     */
    public static final int WAITING = 3;
    /**
     * 余额支付成功
     */
    public static final int BALANCESUSSESS = 4;
    /**
     * 缓存默认时间
     */
    public static final int JEDIS_EXPIRE = 60 * 60;

    private RedisConstants() {
    }

}
