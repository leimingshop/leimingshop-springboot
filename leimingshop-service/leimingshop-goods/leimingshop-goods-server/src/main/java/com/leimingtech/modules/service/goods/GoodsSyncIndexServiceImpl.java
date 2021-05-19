/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * 商品索引同步Service
 *
 * @author xuzhch
 * @email 1197793912@qq.com
 * @since 1.0.0 2019年11月16日
 */
@Slf4j
@Service

public class GoodsSyncIndexServiceImpl implements GoodsSyncIndexService {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired

    private GoodsSpecService goodsSpecService;

    /**
     * 根据商品ID 同步商品与规格索引
     *
     * @param goodsId 商品ID
     * @date 2020/1/10/010 9:55
     * @author xuzhch
     **/
    @Override

    public void goodsIndexSyncByGoodsId(Long goodsId) {
        if (goodsId != null) {
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_SINGLE, String.valueOf(goodsId));
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE, JSON.toJSONString(Arrays.asList(goodsId)));
        }
    }


    /**
     * 发送消息更新商品与规格索引
     *
     * @param goodsIds: 商品ID集合
     * @date 2020/1/6 14:33
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public void batchGoodsIndexSync(@RequestBody List<Long> goodsIds) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_BATCH_UPDATE, JSON.toJSONString(goodsIds));
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE, JSON.toJSONString(goodsIds));
    }

    /**
     * 根据店铺ID同步商品与规格索引
     *
     * @param storeId 店铺ID
     * @date 2020/1/10/010 9:54
     * @author xuzhch
     **/
    @Override

    public void goodsIndexSyncByStoreId(Long storeId) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_GOODS_INDEX_UPDATE, String.valueOf(storeId));
    }

    /**
     * 根据规格ID同步索引
     *
     * @param specIds 规格ID集合
     * @date 2020/1/10/010 9:53
     * @author xuzhch
     **/
    @Override

    public void specIndexSyncBySpecIds(@RequestBody List<Long> specIds) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_SINGLE, JSONArray.toJSONString(specIds));

    }

    /**
     * 同步规格价格索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/

    @Override
    public void specPriceIndexUpdate(@RequestBody List<Long> specIds, @RequestParam("goodsId") Long goodsId) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_PRICE_INDEX_UPDATE, JSONArray.toJSONString(specIds));
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_SINGLE, String.valueOf(goodsId));

    }

    /**
     * 同步规格库存索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/

    @Override
    public void specStorageIndexUpdate(@RequestBody List<Long> specIds) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_STORAGE_INDEX_UPDATE, JSONArray.toJSONString(specIds));
    }

    /**
     * 同步规格上下架状态索引
     *
     * @param specIds 规格ID集合
     * @return
     * @date 2020年5月4日19:46:57
     * @author xuzhch
     **/

    @Override
    public void specShowIndexSyncUpdate(@RequestBody List<Long> specIds) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_SHOW_INDEX_UPDATE, JSONArray.toJSONString(specIds));

    }

}
