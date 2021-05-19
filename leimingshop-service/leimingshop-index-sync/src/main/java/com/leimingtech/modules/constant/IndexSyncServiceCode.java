/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constant;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 索引同步状态码
 */
public class IndexSyncServiceCode extends ServiceStatusCode {

    public static final IndexSyncServiceCode INDEX_SYNC_ERROR = new IndexSyncServiceCode("500101", "商品索引同步失败", new Object[0]);

    public static final IndexSyncServiceCode CREATE_INDEX_ERROR = new IndexSyncServiceCode("500102", "创建商品索引失败", new Object[0]);

    public static final IndexSyncServiceCode CREATE_DELETE_INDEX_ERROR = new IndexSyncServiceCode("500103", "删除索引失败", new Object[0]);

    public static final IndexSyncServiceCode INDEX_SPEC_SYNC_ERROR = new IndexSyncServiceCode("500104", "商品规格索引同步失败", new Object[0]);

    public static final IndexSyncServiceCode CREATE_INDEX_EXISTS = new IndexSyncServiceCode("200102", "索引已经存在，忽略创建索引", new Object[0]);

    public static final IndexSyncServiceCode UPDATE_INDEX_SUCCESS = new IndexSyncServiceCode("200101", "更新索引成功", new Object[0]);

    public static final IndexSyncServiceCode UPDATE_INDEX_IS_NOT_FINISH = new IndexSyncServiceCode("200103", "索引更新未结束，不能同时更新", new Object[0]);

    public static final IndexSyncServiceCode UPDATE_GOODS_INDEX_BY_STOREID_ERROR = new IndexSyncServiceCode("500105", "根据店铺ID更新索引失败", new Object[0]);

    public IndexSyncServiceCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
