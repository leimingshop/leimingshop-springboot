/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.constant;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 搜索状态码
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/11/26 21:23
 **/
public class SearchServiceCode extends ServiceStatusCode {

    public static final SearchServiceCode GOODS_SEARCH_ERROR = new SearchServiceCode("500601",
            "商品索引查询失败", new Object[0]);

    public static final SearchServiceCode GOODS_FILTER_SEARCH_ERROR = new SearchServiceCode("500602",
            "商品筛选索引查询失败", new Object[0]);

    public static final SearchServiceCode GOODS_SPEC_ID_SEARCH_ERROR = new SearchServiceCode("500603",
            "根据规格ID查询商品索引失败", new Object[0]);

    public static final SearchServiceCode STORE_FILTER_SEARCH_ERROR = new SearchServiceCode("500604",
            "店铺索引查询失败", new Object[0]);


    public SearchServiceCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
