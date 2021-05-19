/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.dao.goods.search.GoodsSearchDao;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.goods.search.GoodsSearchDTO;
import com.leimingtech.modules.index.goods.service.GoodsIndexService;
import com.leimingtech.modules.index.goods.service.GoodsSpecIndexService;
import com.leimingtech.modules.utils.EsDataUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * 测试同步索引
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/1/10 14:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexSyncTest {

    @Autowired
    private GoodsIndexService goodsIndexService;
    @Autowired
    private GoodsSpecIndexService goodsSpecIndexService;

    @Autowired
    private GoodsSearchDao goodsSearchDao;

    @Autowired
    private EsDataUtils esDataUtils;

    /**
     * 测试单个保存索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void saveData() {
        List<Long> goodsIds = Arrays.asList(1191627893269078019L);
        List<GoodsSearchDTO> byGoodsIds = goodsSearchDao.findByGoodsIds(goodsIds);
        boolean b = esDataUtils.saveData(IndexContant.GOODS_INDEX_NAME, "1191627893269078019", JSONObject.toJSON(byGoodsIds.get(0)).toString(), GoodsSearchDTO.class);
    }

    /**
     * 测试批量保存索引（同步）
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void saveDataBatch() {
        List<Long> goodsIds = Arrays.asList(1191627893269078019L, 1191898904778555394L);
        List<GoodsSearchDTO> byGoodsIds = goodsSearchDao.findByGoodsIds(goodsIds);
        boolean b = esDataUtils.saveDataBatch(IndexContant.GOODS_INDEX_NAME, "id", JSONObject.toJSON(byGoodsIds).toString(), GoodsSearchDTO.class);
    }

    /**
     * 测试批量保存索引（异步）
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void saveDataBatchAsync() {
        List<Long> goodsIds = Arrays.asList(1191627893269078019L, 1191898904778555394L);
        List<GoodsSearchDTO> byGoodsIds = goodsSearchDao.findByGoodsIds(goodsIds);
        boolean b = esDataUtils.saveDataBatchAsync(IndexContant.GOODS_INDEX_NAME, "id", JSONObject.toJSON(byGoodsIds).toString());
    }

    /**
     * 测试修改单个索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void updateData() {
        List<Long> goodsIds = Arrays.asList(1191627893269078019L);
        List<GoodsSearchDTO> byGoodsIds = goodsSearchDao.findByGoodsIds(goodsIds);
        boolean b = esDataUtils.updateData(IndexContant.GOODS_INDEX_NAME, "1191627893269078019", JSONObject.toJSON(byGoodsIds.get(0)).toString());
    }

    /**
     * 测试修改批量索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void updateDataBatch() {
        List<Long> goodsIds = Arrays.asList(1191627893269078019L, 1191898904778555394L);
        List<GoodsSearchDTO> byGoodsIds = goodsSearchDao.findByGoodsIds(goodsIds);
        boolean b = esDataUtils.updateDataBatch(IndexContant.GOODS_INDEX_NAME, "id", JSONObject.toJSONString(byGoodsIds));
    }

    /**
     * 测试删除索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void deleteDate() {
        boolean b = esDataUtils.deleteDate(IndexContant.GOODS_INDEX_NAME, "1191627893269078019");
    }

    /**
     * 测试批量删除索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void bulkDelete() {
        List<Long> goodsIds = Arrays.asList(1191627893269078019L, 1191898904778555394L);
        boolean b = esDataUtils.bulkDelete(IndexContant.GOODS_INDEX_NAME, goodsIds);
    }

    /**
     * 测试清空索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void deleteAll() {
        boolean b = esDataUtils.deleteAll("goods");
    }

    /**
     * 测试根据ID查询索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void getDateById() {
        String result = esDataUtils.getDateById(IndexContant.GOODS_INDEX_NAME, "1191627893269078019");
    }

    /**
     * 测试ES 商品批量同步
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void goodsIndexSyncTest() {
        boolean result = goodsIndexService.goodsIndexSync();
    }

    /**
     * 测试 ES 商品批量同步（指定商品ID集合）
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void batchGoodsIndexSyncTest() {
        ArrayList<Long> objects = new ArrayList<>();
        objects.add(1191662743799734274L);
        objects.add(1191898904778555394L);
        boolean result = goodsIndexService.goodsIndexBatchSync(objects);
    }

    @Test
    public void goodsIndexSyncGoodsIdTest() {
        boolean result = goodsIndexService.goodsIndexSyncByGoodsId(1191627893269078019L);
    }

    @Test
    public void goodsSpecIndexSync() {
        Map<String, Object> map = new HashMap<>(10);
        map.put("1249892580334882823", 0);
        boolean b = goodsSpecIndexService.updateSaleNum(map);
        System.out.println(b);
//        boolean result = goodsSpecIndexService.goodsIndexSpecSync();
    }


    /**
     * 测试ES修改删除
     *
     * @date 2020/1/10 14:44
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void testUpdateByQuery() {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("id", 1191627893269078019L);

        Map<String, Object> map = new HashMap<>();
        map.put("goodsName", "lixiang2");
        map.put("gcName", "lixiang2");
        esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.GOODS_INDEX, JSON.toJSONString(map));
    }

    /**
     * 测试ES删除查询
     *
     * @date 2020/1/10 14:44
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void testDeleteByQuery() {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("id", 1191627893269078019L);

        esDataUtils.deleteByQuery(pageModelDTO, ElasticSearchConstant.GOODS_INDEX);
    }

    @Test
    public void testCountData() {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("id", 1191627893269078019L);
        Long aLong = esDataUtils.countData(pageModelDTO, ElasticSearchConstant.GOODS_INDEX, null);
        System.out.println(aLong);
    }

    @Test
    public void testSumData() {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("storeId", 1191892697665863681L);
        esDataUtils.sumData(pageModelDTO, "storeId", "sellerSpecPrice", ElasticSearchConstant.GOODS_INDEX);
    }
}
