/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constant;

/**
 * ES常量类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/11 10:39
 **/
public class ElasticSearchConstant {

    /**
     * 查询多字段分割标识
     */
    public final static String SPLIT_FLAG = ",";

    /**
     * 排序规则-升序
     */
    public final static String SORT_ASC = "asc";

    /**
     * 排序规则-降序
     */
    public final static String DESC_ASC = "desc";

    /**
     * 商品索引名称
     */
    public static final String GOODS_INDEX = "goods";

    public static final String GOODS_SPEC_INDEX = "goods_spec";

    public static final String STORE_INDEX = "store";

    /**
     * 优惠券活动索引
     */
    public static final String COUPONS_ACTIVITY_INDEX = "coupons_activity";

    /**
     * 会员优惠券索引
     */
    public static final String MEMBER_COUPONS_INDEX = "member_coupons";

    /**
     * 满减活动索引
     */
    public static final String REDUCE_ACTIVITY_INDEX = "reduce_activity";

    /**
     * 购物车索引
     */
    public static final String CART_INDEX = "cart";
}
