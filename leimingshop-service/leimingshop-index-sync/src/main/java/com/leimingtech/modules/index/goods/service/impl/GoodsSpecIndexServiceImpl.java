/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.constant.IndexSyncServiceCode;
import com.leimingtech.modules.dao.goods.search.GoodsSearchDao;
import com.leimingtech.modules.dao.store.search.StoreSearchDao;
import com.leimingtech.modules.dto.goods.search.GoodsSearchDTO;
import com.leimingtech.modules.dto.spec.search.GoodsVO;
import com.leimingtech.modules.dto.spec.search.SpecAttrValueRefVO;
import com.leimingtech.modules.dto.spec.search.SpecVO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.index.forkjoin.GoodSpecIndexTask;
import com.leimingtech.modules.index.goods.service.GoodsSpecIndexService;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 8:52 PM
 */
@Slf4j
@Service
public class GoodsSpecIndexServiceImpl implements GoodsSpecIndexService {

    private static MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(GoodsIndexServiceImpl.class);


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private GoodsSearchDao goodsSearchDao;

    @Autowired
    private StoreSearchDao storeSearchDao;


    /**
     * ES 商品规格批量同步
     *
     * @return 操作结果
     * @date 2019/12/11 18:36
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean goodsIndexSpecSync() {
        String lockKey = IndexContant.REDIS_PREFIX + IndexContant.GOODS_SPEC_INDEX_SYNC_LOCK;
        String lastSyncTimeKey = IndexContant.REDIS_PREFIX + IndexContant.GOODS_SPEC_INDEX_LAST_SYNC_TIME;

        boolean isGetLock = redisUtils.tryLock(lockKey);
        if (isGetLock) {
            try {
                //获取上次同步时间
                Long lastTime = (Long) redisUtils.get(lastSyncTimeKey);
                Long currentTime = System.currentTimeMillis();

                int total = goodsSearchDao.getTotalGoodsSpecIndex(lastTime == null ? null : new Date(lastTime), new Date(currentTime));
                ForkJoinPool forkJoinPool = new ForkJoinPool(4);

                if (null == lastTime) {
                    lastTime = 9999999999999L;
                }
                GoodSpecIndexTask indexTask = new GoodSpecIndexTask(0, total, lastTime, currentTime);

                forkJoinPool.submit(indexTask);

                forkJoinPool.shutdown();

                redisUtils.set(lastSyncTimeKey, System.currentTimeMillis());

                log.info("共{}条数据", total);
                return true;
            } finally {
                redisUtils.releaseLock(lockKey);
            }
        } else {
            logger.warn(IndexSyncServiceCode.UPDATE_INDEX_IS_NOT_FINISH.getCode(), IndexSyncServiceCode.UPDATE_INDEX_IS_NOT_FINISH.getMessage());
            return false;
        }
    }


    /**
     * ES 规格批量同步（指定规格ID集合）
     *
     * @param specIds: 规格ID集合
     * @return 返回操作结果
     * @date 2019/12/11 18:40
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean goodsSpecIndexSyncBySpecsId(List<Long> specIds) {
        try {
            // 单个更新商品规格索引用字段
            List<SpecVO> specVOList = goodsSearchDao.listSpec(null, null, null, specIds);
            for (SpecVO specVO : specVOList) {
                StoreDTO storeDTO = storeSearchDao.get(specVO.getGoodsVO().getStoreId());
                if (storeDTO != null) {
                    String gradeName = storeSearchDao.getNameById(storeDTO.getGradeId());
                    specVO.getGoodsVO().setStoreGrade(gradeName);
                    specVO.getGoodsVO().setStoreLogo(storeDTO.getStoreLogo());
                    // 不更新活动es
                    specVO.setSpecActivityList(null);
                }
            }
            //list 空值判断
            if (CollectionUtils.isEmpty(specVOList)) {
                log.info("暂无数据更新");
                return false;
            }
            return esDataUtils.updateDataBatch(IndexContant.GOODS_SPEC_INDEX_NAME, "id", JSON.toJSONString(specVOList), SpecVO.class);
        } catch (Exception e) {
            logger.info(IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getCode(), IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getMessage(), e);
        }
        return false;
    }


    /**
     * ES 根据商品ID同步规格索引
     *
     * @param goodsIdList: 商品ID集合
     * @return 操作结果
     * @date 2019/12/11 18:42
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean goodsSpecIndexSyncByGoodsId(List<Long> goodsIdList) {
        try {
            goodsIdList.forEach(goodsId -> {
                // 单个更新商品规格索引用字段
                List<SpecVO> specVOList = goodsSearchDao.listSpec(null, null, goodsId, null);

                for (SpecVO specVO : specVOList) {
                    StoreDTO storeDTO = storeSearchDao.get(specVO.getGoodsVO().getStoreId());
                    if (storeDTO != null) {
                        String gradeName = storeSearchDao.getNameById(storeDTO.getGradeId());
                        specVO.getGoodsVO().setStoreGrade(gradeName);
                        specVO.getGoodsVO().setStoreLogo(storeDTO.getStoreLogo());
                        // 不更新活动es
                        specVO.setSpecActivityList(null);
                    }
                }

//                esDataUtils.saveDataBatch(IndexContant.GOODS_SPEC_INDEX_NAME, "id", JSON.toJSONString(specVOList), SpecVO.class);
                esDataUtils.updateDataBatch(IndexContant.GOODS_SPEC_INDEX_NAME, "id", JSON.toJSONString(specVOList), SpecVO.class);
                // 删除商品规格索引
                List<Long> specIds = goodsSearchDao.findDeletedSpec(goodsId);
                if (CollectionUtils.isNotEmpty(specIds)) {
                    esDataUtils.bulkDelete(IndexContant.GOODS_SPEC_INDEX_NAME, specIds);
                }
            });
            return true;
        } catch (Exception e) {
            logger.info(IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getCode(), IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getMessage(), e);
        }
        return false;
    }

    /**
     * 更新规格索引销量值
     *
     * @param map: key为specId value销量值  正为增加负为减少
     * @return 操作结果
     * @date 2020/4/13 11:47
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean updateSaleNum(Map<String, Object> map) {

        List<SpecVO> list = Lists.newArrayList();
        // 遍历所有数据
        map.keySet().forEach(key -> {
            // 规格值
            Long specId = Long.parseLong(key);
            // 销量 正为增加负为减少
            Integer saleNum = Integer.parseInt(map.get(key).toString());
            SpecVO specVO = new SpecVO();
            specVO.setId(specId);

            // 如果规格不存在，不更新索引信息
            List<SpecVO> specVOList = goodsSearchDao.listSpec(null, null, null, Arrays.asList(specId));
            if (CollectionUtils.isNotEmpty(specVOList)) {
                specVO.setSpecSaleNum(saleNum);
                specVO.setSpecActivityList(null);
                list.add(specVO);
            }
        });
        return esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_SPEC_INDEX, "id", JSONArray.toJSONString(list), SpecVO.class);
    }

    /**
     * 更新规格价格索引
     *
     * @param specIds
     * @author xuzhch
     * @date 2020年5月4日18:35:13
     */
    @Override
    public boolean updatePrice(List<Long> specIds) {
//        goodsSearchDao.listSpecPrice(null,null,null,specIds);
        Integer updatePriceFlag = 0;
        List<SpecVO> specVOList = goodsSearchDao.listSpecPriceOrStorage(updatePriceFlag, specIds);
        return esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_SPEC_INDEX, "id", JSONArray.toJSONString(specVOList), SpecVO.class);
    }

    /**
     * 更新规格库存索引
     *
     * @param specIds
     * @author xuzhch
     * @date 2020年5月4日18:35:13
     */
    @Override
    public boolean updateStorage(List<Long> specIds) {
        Integer updateStorageFlag = 1;
        List<SpecVO> specVOList = goodsSearchDao.listSpecPriceOrStorage(updateStorageFlag, specIds);
        for (SpecVO specVO : specVOList) {
            GoodsVO goodsVO = new GoodsVO();
            ArrayList<SpecAttrValueRefVO> vos = new ArrayList<>();
            SpecAttrValueRefVO specAttrValueRefVO = new SpecAttrValueRefVO();
            specAttrValueRefVO.setSpecStorage(specVO.getSpecStorage());
            vos.add(specAttrValueRefVO);
            goodsVO.setSpecAttrValueRefVO(vos);
            specVO.setGoodsVO(goodsVO);
        }

//        goodsVO.specAttrValueRefVO.specStorage
        return esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_SPEC_INDEX, "id", JSONArray.toJSONString(specVOList), SpecVO.class);
    }

    /**
     * 更新规格上下架状态索引
     *
     * @param specIds
     * @author xuzhch
     * @date 2020年5月4日18:35:13
     */
    @Override
    public boolean updateShow(List<Long> specIds) {
        Integer updateShowFlag = 2;
        List<SpecVO> specVOList = goodsSearchDao.listSpecPriceOrStorage(updateShowFlag, specIds);
        GoodsSearchDTO goodsDTO = goodsSearchDao.getGoodsIndexInfoByGoodsId(specVOList.get(0).getGoodsId());
        GoodsSearchDTO goodsSearchDTO = new GoodsSearchDTO();
        goodsSearchDTO.setId(specVOList.get(0).getId());
        goodsSearchDTO.setGoodsShow(goodsDTO.getGoodsShow());
        for (SpecVO specVO : specVOList) {
            GoodsVO goodsVO = new GoodsVO();
            ArrayList<SpecAttrValueRefVO> vos = new ArrayList<>();
            SpecAttrValueRefVO specAttrValueRefVO = new SpecAttrValueRefVO();
            specAttrValueRefVO.setSpecShow(specVO.getSpecShow());
            vos.add(specAttrValueRefVO);
            goodsVO.setSpecAttrValueRefVO(vos);
            specVO.setGoodsVO(goodsVO);
        }

//        goodsVO.specAttrValueRefVO.specShow
        esDataUtils.updateData(ElasticSearchConstant.GOODS_INDEX, String.valueOf(goodsSearchDTO.getId()), JSONArray.toJSONString(goodsSearchDTO));
        return esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_SPEC_INDEX, "id", JSONArray.toJSONString(specVOList), SpecVO.class);

    }
}
