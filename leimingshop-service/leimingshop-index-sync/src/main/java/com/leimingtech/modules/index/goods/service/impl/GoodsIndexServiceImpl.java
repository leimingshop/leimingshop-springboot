/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.constant.IndexSyncServiceCode;
import com.leimingtech.modules.dao.goods.search.GoodsSearchDao;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.goods.search.GoodsSearchDTO;
import com.leimingtech.modules.index.goods.service.GoodsIndexService;
import com.leimingtech.modules.index.goods.service.GoodsSpecIndexService;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 8:52 PM
 */
@Slf4j
@Service
public class GoodsIndexServiceImpl implements GoodsIndexService {

    private static MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(GoodsIndexServiceImpl.class);
    private final int MAX_SEND = 100;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private EsDataUtils esDataUtils;
    @Autowired
    private GoodsSearchDao goodsSearchDao;
    @Autowired
    private GoodsSpecIndexService goodsSpecIndexService;
    @Autowired
    private NosqlService nosqlService;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * ES 商品批量同步
     *
     * @return 操作结果
     * @date 2019/12/11 18:34
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean goodsIndexSync() {
        String lockKey = IndexContant.REDIS_PREFIX + IndexContant.GOODS_INDEX_SYNC_LOCK;
        String lastSyncTimeKey = IndexContant.REDIS_PREFIX + IndexContant.GOODS_INDEX_LAST_SYNC_TIME;

        // 先获取商品索引同步状态
        boolean isGetLock = redisUtils.tryLock(lockKey);
        if (isGetLock) {
            // 商品索引未在同步
            try {
                //获取上次同步时间
                Long lastTime = (Long) redisUtils.get(lastSyncTimeKey);
                Long currentTime = System.currentTimeMillis();
//                int total = goodsSearchDao.getTotalGoodsIndex(lastTime == null ? null : new Date(lastTime), new Date(currentTime));
//                ForkJoinPool forkJoinPool = new ForkJoinPool(4);
//
//                if (null == lastTime) {
//                    lastTime = 9999999999999L;
//                }
//                GoodIndexTask indexTask = new GoodIndexTask(0, total, lastTime, currentTime);
//
//                forkJoinPool.submit(indexTask);
//
//                forkJoinPool.shutdown();
//
//                redisUtils.set(lastSyncTimeKey, System.currentTimeMillis());
//                log.info("共{}条数据", total);
//                return true;

                // 查询上次同步至今商品修改集合
                List<GoodsSearchDTO> goodsSearchDTOList = goodsSearchDao.selectGoodsIndexInfoByUpdateTime(lastTime == null ? null : new Date(lastTime),
                        new Date(currentTime));
                //list 空值判断
                if (CollectionUtils.isEmpty(goodsSearchDTOList)) {
                    log.info("暂无数据更新");
                    return false;
                }
                // 分割处理list
                final int limit = this.countStep(goodsSearchDTOList.size(), MAX_SEND);
                Stream.iterate(0, n -> n + 1).limit(limit).forEach(a -> {
                    List<GoodsSearchDTO> dataList = goodsSearchDTOList.stream().skip(a * MAX_SEND).limit(MAX_SEND).parallel().collect(Collectors.toList());
                    dataList.stream().parallel().forEach(goodsSearchDTO -> {
                        log.info("开始执行第{}页", a);
                        System.out.println("开始执行第" + a + "页");
                        String avgGoodsEvaluate = goodsSearchDao.reputably(goodsSearchDTO.getId());
                        goodsSearchDTO.setGoodEvaluate(avgGoodsEvaluate);
                        goodsSearchDTO.setGoodsType(Integer.valueOf(goodsSearchDTO.getStoreType()));
                        goodsSearchDTO.setCollectNum(goodsSearchDao.findGoodsCollectNum(goodsSearchDTO.getId()));
                        Map<String, Object> map = new HashMap<>();
                        map.put("goodsId", goodsSearchDTO.getId());
                        goodsSearchDTO.setBrowseNum(nosqlService.queryData("member_browse_record", map).size());
                        List<String> keywordTipsList = new ArrayList<>();
                        if (StringUtils.isNotBlank(goodsSearchDTO.getBrandName())) {
                            keywordTipsList.add(goodsSearchDTO.getBrandName());
                        }
                        if (StringUtils.isNotBlank(goodsSearchDTO.getGcName())) {
                            keywordTipsList.add(goodsSearchDTO.getGcName());
                        }
                        goodsSearchDTO.setKeywordTips(keywordTipsList);
                    });
                    log.info("数据量为{}", dataList.size());
                    // 批量进行商品索引保存
                    boolean result = esDataUtils.saveDataBatch(IndexContant.GOODS_INDEX_NAME, "id", JSON.toJSONString(dataList), GoodsSearchDTO.class);
                    if (result) {
                        redisUtils.set(lastSyncTimeKey, System.currentTimeMillis());
                        log.info("第{}页保存商品索引成功", a);
                    }
                });
                return true;
            } finally {
                redisUtils.releaseLock(lockKey);
            }
        } else {
            logger.warn(IndexSyncServiceCode.UPDATE_INDEX_IS_NOT_FINISH.getCode(), IndexSyncServiceCode.UPDATE_INDEX_IS_NOT_FINISH.getMessage());
        }
        return false;
    }

    private int countStep(int size, int maxSend) {
        return (size + maxSend - 1) / maxSend;
    }

    /**
     * ES 商品批量同步（指定商品ID集合）
     *
     * @param goodsIds: 商品ID集合
     * @date 2019/11/19 18:15
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean goodsIndexBatchSync(List<Long> goodsIds) {
        try {
            List<GoodsSearchDTO> goodsSearchDTOS = goodsSearchDao.findByGoodsIds(goodsIds);

            //list 空值判断
            if (CollectionUtils.isEmpty(goodsSearchDTOS)) {
                log.info("暂无数据更新");
                return false;
            }
            goodsSearchDTOS.forEach(goodsSearchDTO -> {
                List<String> keywordTipsList = new ArrayList<>();
                if (StringUtils.isNotBlank(goodsSearchDTO.getBrandName())) {
                    keywordTipsList.add(goodsSearchDTO.getBrandName());
                }
                if (StringUtils.isNotBlank(goodsSearchDTO.getGcName())) {
                    keywordTipsList.add(goodsSearchDTO.getGcName());
                }
                goodsSearchDTO.setKeywordTips(keywordTipsList);
            });
            return esDataUtils.saveDataBatch(IndexContant.GOODS_INDEX_NAME, "id",
                    JSON.toJSONString(goodsSearchDTOS), GoodsSearchDTO.class);
        } catch (Exception e) {
            logger.info(IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getCode(), IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getMessage(), e);
            return false;
        }
    }

    /**
     * ES 根据商品ID更新商品索引
     *
     * @param goodsId: 商品ID
     * @return 操作结果
     * @date 2019/12/11 18:38
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean goodsIndexSyncByGoodsId(Long goodsId) {
        GoodsSearchDTO goodsSearchDTO = goodsSearchDao.getGoodsIndexInfoByGoodsId(goodsId);
        if (goodsSearchDTO != null) {
            List<String> keywordTipsList = new ArrayList<>();
            if (StringUtils.isNotBlank(goodsSearchDTO.getBrandName())) {
                keywordTipsList.add(goodsSearchDTO.getBrandName());
            }
            if (StringUtils.isNotBlank(goodsSearchDTO.getGcName())) {
                keywordTipsList.add(goodsSearchDTO.getGcName());
            }
            goodsSearchDTO.setKeywordTips(keywordTipsList);

            goodsSearchDTO.setGoodsType(Integer.valueOf(goodsSearchDTO.getStoreType()));
            return esDataUtils.updateData(IndexContant.GOODS_INDEX_NAME, String.valueOf(goodsId), JSON.toJSONString(goodsSearchDTO));
        }
        return false;
    }


    /**
     * ES 根据店铺ID同步商品与规格索引
     *
     * @param storeId: 店铺ID
     * @return 操作结果
     * @date 2019/12/11 18:43
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean goodsIndexSyncByStoreId(String storeId) {

        // 查询出商品数据
        List<GoodsSearchDTO> goodsSearchDTOS = goodsSearchDao.goodsIndexSyncByStoreId(storeId);
        List<Long> goodsIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(goodsSearchDTOS)) {

            // 循环封装好评率信息
            for (GoodsSearchDTO goodsSearchDTO : goodsSearchDTOS) {
                goodsIds.add(goodsSearchDTO.getId());
                // 查询好评率
                String avgGoodsEvaluate = goodsSearchDao.reputably(goodsSearchDTO.getId());
                goodsSearchDTO.setGoodEvaluate(avgGoodsEvaluate);
                goodsSearchDTO.setGoodsType(Integer.valueOf(goodsSearchDTO.getStoreType()));

                List<String> keywordTipsList = new ArrayList<>();
                if (StringUtils.isNotBlank(goodsSearchDTO.getBrandName())) {
                    keywordTipsList.add(goodsSearchDTO.getBrandName());
                }
                if (StringUtils.isNotBlank(goodsSearchDTO.getGcName())) {
                    keywordTipsList.add(goodsSearchDTO.getGcName());
                }
                goodsSearchDTO.setKeywordTips(keywordTipsList);
            }
            esDataUtils.saveDataBatch(IndexContant.GOODS_INDEX_NAME, "id", JSON.toJSONString(goodsSearchDTOS), GoodsSearchDTO.class);
        }

        // 同步规格索引
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            List<Long> specIds = goodsSearchDao.listSpecIdsByGoodsIds(goodsIds);
            goodsSpecIndexService.goodsSpecIndexSyncBySpecsId(specIds);
        }
        return true;
    }

    /**
     * 更新商品索引销量值
     *
     * @param map: key为specId value销量值  正为增加负为减少
     * @return 操作结果
     * @date 2020/4/13 11:47
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean updateSaleNum(Map<String, Object> map) {
        List<GoodsSearchDTO> list = Lists.newArrayList();
        // 遍历所有数据
        map.keySet().forEach(key -> {
            // 规格值
            Long goodsId = Long.parseLong(key);
            // 销量 正为增加负为减少
            Integer saleNum = Integer.parseInt(map.get(key).toString());
            GoodsSearchDTO goodsSearchDTO = new GoodsSearchDTO();
            goodsSearchDTO.setId(goodsId);

            // 如果规格不存在，不更新索引信息
            GoodsSearchDTO goodsDTO = goodsSearchDao.getGoodsIndexInfoByGoodsId(goodsId);
            if (goodsDTO != null) {
                goodsSearchDTO.setGoodsSaleNum(saleNum);
                list.add(goodsSearchDTO);
            }
        });
        return esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_INDEX, "id", JSONArray.toJSONString(list));
    }

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
    @Override
    public boolean updateFreightTemplateId(@RequestParam("oldFreightTemplateId") Long oldFreightTemplateId,
                                           @RequestParam("newFreightTemplateId") Long newFreightTemplateId) {
        // 设置更新条件
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("freightTemplateId", oldFreightTemplateId);

        // 更新结果
        Map<String, Object> params = new HashMap<>();
        params.put("freightTemplateId", newFreightTemplateId);

        return esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.GOODS_INDEX, JSONObject.toJSONString(params));
    }

    /**
     * 更新商品评价数量
     *
     * @param map 商品信息
     * @return
     */
    @Override
    public boolean updateGoodsEvaluate(Map<String, Object> map) {
        Long goodsId = Long.valueOf(map.get("goodsId").toString());
        Integer evaluateNum = Integer.valueOf(map.get("evaluateNum").toString());
        // 好评率
        String reputably = goodsSearchDao.reputably(goodsId);

        // 设置更新条件
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("id", goodsId);

        // 更新结果
        Map<String, Object> params = new HashMap<>(3);
        params.put("evaluateCount", evaluateNum);
        params.put("goodEvaluate", reputably);

        return esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.GOODS_INDEX, JSONObject.toJSONString(params));
    }

    /**
     * 功能描述 更新商品的评价数量 收藏数量 浏览数量
     *
     * @param * @param
     * @return boolean
     * @author lishuo
     * @date 9/7/2020
     */
    @Override
    public boolean goodsInfoIndexSync() {

        // 查询出所有的商品的id的集合
        int pageNum = 1;
        int size = 100;
        Page<List<Long>> page = new Page<>(pageNum, size);
        List<Long> goodsIds = goodsSearchDao.selectGoodsId(page);
        PageData<Long> listPageData = new PageData<Long>(goodsIds, page.getTotal());
        //更新商品索引信息
        Boolean result = updateGoodsInfo(goodsIds);

        if (result) {
            for (long i = 2; i < page.getPages(); i++) {
                pageNum = (int) i;
                page = new Page<>(pageNum, size);
                goodsIds = goodsSearchDao.selectGoodsId(page);
                result = updateGoodsInfo(goodsIds);
                if (!result) {
                    return result;
                }
            }
        }
        return false;
    }

    private Boolean updateGoodsInfo(List<Long> goodsIds) {
        try {
            // 浏览数量的查询
            MatchOperation matchOperation = Aggregation.match(Criteria.where("goodsId").in(goodsIds));
            GroupOperation groupOperation = Aggregation.group("goodsId").count().as("count");
            ProjectionOperation projectionOperation = Aggregation.project("count");
            List<Object> mappedResults = mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation), "member_browse_record", Object.class).getMappedResults();
            Map<String, Object> browseNumMap = new HashMap<>();
            for (Object mappedResult : mappedResults) {
                browseNumMap.put(((Map) mappedResult).get("_id").toString(), ((Map) mappedResult).get("count"));

            }
            log.info(JSON.toJSONString(mappedResults));
            // 查询评论数量
            List<GoodsSearchDTO> goodsEvaluateNum = goodsSearchDao.findGoodsEvaluateNumList(goodsIds);
            // 查询收藏数量
            List<GoodsSearchDTO> goodsCollectNum = goodsSearchDao.findGoodsCollectNumList(goodsIds);
            // 进行封装
            Map<String, Object> goodsCollectNumMap = new HashMap<>();
            for (GoodsSearchDTO goodsSearchDTO : goodsCollectNum) {
                goodsCollectNumMap.put(goodsSearchDTO.getId().toString(), goodsSearchDTO.getCollectNum());
            }
            Map<String, Object> goodsEvaluateNumMap = new HashMap<>();
            for (GoodsSearchDTO goodsSearchDTO : goodsEvaluateNum) {
                goodsEvaluateNumMap.put(goodsSearchDTO.getId().toString(), goodsSearchDTO.getEvaluateCount());
            }

            for (Long goodsId : goodsIds) {
                PageModelDTO pageModelDTO = new PageModelDTO();
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("goodsId", goodsId);
                Map<String, Object> params = new HashMap<>();
                if (goodsEvaluateNumMap.get(goodsId) != null) {
                    params.put("evaluateCount", goodsEvaluateNumMap.get(goodsId));
                }
                if (browseNumMap.get(goodsId) != null) {
                    params.put("browseNum", browseNumMap.get(goodsId));
                }
                if (goodsCollectNumMap.get(goodsId) != null) {
                    params.put("collectNum", goodsCollectNumMap.get(goodsId));
                }
                esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.GOODS_INDEX, JSONObject.toJSONString(params));
            }
            return true;
        } catch (Exception e) {
            log.info("商品信息更新异常" + e);
            return false;
        }
    }
}
