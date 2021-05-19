/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.store.impl;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.constant.IndexSyncServiceCode;
import com.leimingtech.modules.dao.goods.search.GoodsSearchDao;
import com.leimingtech.modules.dao.store.search.StoreSearchDao;
import com.leimingtech.modules.dto.store.StoreSearchDTO;
import com.leimingtech.modules.index.store.StoreIndexService;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Data: 2019/12/5 9:51
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@Service
public class StoreIndexServiceImpl implements StoreIndexService {

    private static MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(StoreIndexServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private StoreSearchDao storeSearchDao;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private GoodsSearchDao goodsSearchDao;

    /**
     * 同步店铺索引
     *
     * @return
     */
    @Override
    public boolean syncStoreIndex() {
        String lockKey = IndexContant.REDIS_PREFIX + IndexContant.STORE_INDEX_SYNC_LOCK;
        String lastSyncTimeKey = IndexContant.REDIS_PREFIX + IndexContant.STORE_INDEX_LAST_SYNC_TIME;

        // 先获取店铺索引同步状态
        boolean isGetLock = redisUtils.tryLock(lockKey);
        if (isGetLock) {
            // 店铺索引未在同步
            try {
                //获取上次同步时间
                Long lastTime = (Long) redisUtils.get(lastSyncTimeKey);
                Long currentTime = System.currentTimeMillis();

                // 查询上次同步至今店铺修改集合
                List<StoreSearchDTO> goodsSearchDTOList = storeSearchDao.selectStoreIndexInfoByUpdateTime(lastTime == null ? null : String.valueOf(new Date(lastTime)),
                        String.valueOf(new Date(currentTime)), null);

                //list 空值判断
                if (CollectionUtils.isEmpty(goodsSearchDTOList)) {
                    log.info("暂无数据更新");
                    return false;
                }
                goodsSearchDTOList.stream().parallel().forEach(storeSearchDTO -> {
                    String avgGoodsEvaluate = goodsSearchDao.storeReputably(storeSearchDTO.getId());
                    // 查询店铺销量
                    Integer saleNum = goodsSearchDao.selectStoreSale(storeSearchDTO.getId());
                    storeSearchDTO.setSaleNum(saleNum == null ? 0 : saleNum);
                    storeSearchDTO.setGoodEvaluate(avgGoodsEvaluate);
                });
                // 批量保存店铺索引（内涵创建索引逻辑）
                boolean saveResult = esDataUtils.saveDataBatch(IndexContant.STORE_INDEX_NAME, "id", JSON.toJSONString(goodsSearchDTOList), StoreSearchDTO.class);
                redisUtils.set(lastSyncTimeKey, currentTime);
                return saveResult;
            } finally {
                redisUtils.releaseLock(lockKey);
            }
        } else {
            logger.warn(IndexSyncServiceCode.UPDATE_INDEX_IS_NOT_FINISH.getCode(), IndexSyncServiceCode.UPDATE_INDEX_IS_NOT_FINISH.getMessage());
            return false;
        }
    }

    /**
     * 根据店铺ID更新店铺索引
     *
     * @param storeId
     * @return
     */
    @Override
    public boolean syncStoreIndexByStoreId(Long storeId) {

        List<StoreSearchDTO> goodsSearchDTOList = storeSearchDao.selectStoreIndexInfoByUpdateTime(null, null, storeId);

        if (CollectionUtils.isNotEmpty(goodsSearchDTOList)) {
            // 查询店铺销量
            Integer saleNum = goodsSearchDao.selectStoreSale(goodsSearchDTOList.get(0).getId());
            goodsSearchDTOList.get(0).setSaleNum(saleNum == null ? 0 : saleNum);
            // 批量更新店铺索引
            return esDataUtils.updateDataBatch(IndexContant.STORE_INDEX_NAME, "id", JSON.toJSONString(goodsSearchDTOList));
        }
        return false;
    }

    /**
     * 删除店铺索引
     *
     * @param storeId
     * @return
     */
    @Override
    public boolean deleteStoreIndex(Long storeId) {
        return esDataUtils.deleteDate(IndexContant.STORE_INDEX_NAME, String.valueOf(storeId));
    }

}
