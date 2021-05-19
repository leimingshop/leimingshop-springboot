/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.store;

/**
 * @Description
 * @Data: 2019/12/5 9:49
 * @Author: chengqian
 * @Version: 1.0
 */
public interface StoreIndexService {

    /**
     * 同步店铺索引
     *
     * @return
     */
    boolean syncStoreIndex();

    /**
     * 根据店铺ID 更新索引
     */
    boolean syncStoreIndexByStoreId(Long storeId);

    /**
     * 删除店铺索引
     */
    boolean deleteStoreIndex(Long storeId);
}
