/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.goods.service;

import java.util.List;
import java.util.Map;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 8:49 PM
 */
public interface GoodsSpecIndexService {

    /**
     * ES 商品规格批量同步
     *
     * @return 操作结果
     * @date 2019/12/11 18:36
     * @author lixiangx@leimingtech.com
     **/
    boolean goodsIndexSpecSync();

    /**
     * ES 根据商品ID同步规格索引
     *
     * @param goodsIdList: 商品ID集合
     * @return 操作结果
     * @date 2019/12/11 18:42
     * @author lixiangx@leimingtech.com
     **/
    boolean goodsSpecIndexSyncByGoodsId(List<Long> goodsIdList);

    /**
     * ES 规格批量同步（指定规格ID集合）
     *
     * @param specIds: 规格ID集合
     * @return 返回操作结果
     * @date 2019/12/11 18:40
     * @author lixiangx@leimingtech.com
     **/
    boolean goodsSpecIndexSyncBySpecsId(List<Long> specIds);


    /**
     * 更新规格索引销量值
     *
     * @param map: key为specId value销量值  正为增加负为减少
     * @return 操作结果
     * @date 2020/4/13 11:47
     * @author lixiangx@leimingtech.com
     **/
    boolean updateSaleNum(Map<String, Object> map);

    /**
     * 同步规格价格索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/
    boolean updatePrice(List<Long> specIds);

    /**
     * 同步规格库存索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/
    boolean updateStorage(List<Long> specIds);

    /**
     * 同步规格上下架状态索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/
    boolean updateShow(List<Long> specIds);
}
