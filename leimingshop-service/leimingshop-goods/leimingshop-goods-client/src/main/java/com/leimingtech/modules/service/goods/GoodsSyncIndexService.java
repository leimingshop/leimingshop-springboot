/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019年11月16日
 */

public interface GoodsSyncIndexService {

    /**
     * 根据商品ID 同步商品与规格索引
     *
     * @param goodsId 商品ID
     * @date 2020/1/10/010 9:55
     * @author xuzhch
     **/

    void goodsIndexSyncByGoodsId(Long goodsId);

    /**
     * 发送消息更新商品与规格索引
     *
     * @param goodsIds: 商品ID集合
     * @date 2020/1/6 14:33
     * @author lixiangx@leimingtech.com
     **/

    void batchGoodsIndexSync(@RequestBody List<Long> goodsIds);

    /**
     * 根据店铺ID同步商品与规格索引
     *
     * @param storeId 店铺ID
     * @date 2020/1/10/010 9:54
     * @author xuzhch
     **/

    void goodsIndexSyncByStoreId(Long storeId);

    /**
     * 根据规格ID同步索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020/1/10/010 9:53
     * @author xuzhch
     **/

    void specIndexSyncBySpecIds(@RequestBody List<Long> specIds);

    /**
     * 同步规格价格索引
     *
     * @param specIds 规格ID集合
     * @param goodsId 商品id
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/

    void specPriceIndexUpdate(@RequestBody List<Long> specIds, @RequestParam("goodsId") Long goodsId);

    /**
     * 同步规格库存索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/

    void specStorageIndexUpdate(@RequestBody List<Long> specIds);

    /**
     * 同步规格上下架状态索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/

    void specShowIndexSyncUpdate(@RequestBody List<Long> specIds);
}