/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.goods.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 8:49 PM
 */
@Service
public interface GoodsIndexService {


    /**
     * ES 商品批量同步
     *
     * @return 操作结果
     * @date 2019/12/11 18:34
     * @author lixiangx@leimingtech.com
     **/
    boolean goodsIndexSync();

    /**
     * ES 商品批量同步（指定商品ID集合）
     *
     * @param goodsIds: 商品ID集合
     * @date 2019/11/19 18:15
     * @author lixiangx@leimingtech.com
     **/
    boolean goodsIndexBatchSync(List<Long> goodsIds);


    /**
     * ES 根据商品ID更新商品索引
     *
     * @param goodsId: 商品ID
     * @return 操作结果
     * @date 2019/12/11 18:38
     * @author lixiangx@leimingtech.com
     **/
    boolean goodsIndexSyncByGoodsId(Long goodsId);


    /**
     * ES 根据店铺ID同步商品与规格索引
     *
     * @param storeId: 店铺ID
     * @return 操作结果
     * @date 2019/12/11 18:43
     * @author lixiangx@leimingtech.com
     **/
    boolean goodsIndexSyncByStoreId(String storeId);


    /**
     * 更新商品索引销量值
     *
     * @param map: key为specId value销量值  正为增加负为减少
     * @return 操作结果
     * @date 2020/4/13 11:47
     * @author lixiangx@leimingtech.com
     **/
    boolean updateSaleNum(Map<String, Object> map);

    /**
     * 功能描述：
     * <更新商品es运费模板id>
     *
     * @param oldFreightTemplateId 原运费模板id
     * @param newFreightTemplateId 新运费模板id
     * @return
     * @date 2020/5/8 16:55
     * @author 刘远杰
     **/
    boolean updateFreightTemplateId(@RequestParam("oldFreightTemplateId") Long oldFreightTemplateId,
                                    @RequestParam("newFreightTemplateId") Long newFreightTemplateId);


    /**
     * 更新商品评价数量
     *
     * @param map 商品信息
     * @return
     */
    boolean updateGoodsEvaluate(Map<String, Object> map);

    /**
     * 功能描述 批量更新商品的评论 收藏 浏览
     *
     * @return boolean
     * @author lishuo
     * @date 9/7/2020
     */
    boolean goodsInfoIndexSync();
}
