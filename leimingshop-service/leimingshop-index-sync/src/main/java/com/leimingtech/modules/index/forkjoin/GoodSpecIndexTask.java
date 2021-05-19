/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.forkjoin;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.dao.goods.search.GoodsSearchDao;
import com.leimingtech.modules.dao.store.search.StoreSearchDao;
import com.leimingtech.modules.dto.spec.search.GoodsVO;
import com.leimingtech.modules.dto.spec.search.SpecVO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class GoodSpecIndexTask extends RecursiveTask<Void> {

    // 阈值
    private static final int limit = 100;

    private int start;

    private int end;

    private long lastTime;

    private long currentTime;

    public GoodSpecIndexTask(int start, int end, long lastTime, long currentTime) {
        this.start = start;
        this.end = end;
        this.lastTime = lastTime;
        this.currentTime = currentTime;
    }

    @Override
    protected Void compute() {
        if (end - start < limit) {
            log.info("开始执行{}，{}，{}，{}", start, end, lastTime, currentTime);
            initIndex(start, end, lastTime, currentTime);
        } else {
            //如果当前任务数量超出阈值，则进行任务拆分
            int middle = (start + end) / 2;
            GoodSpecIndexTask left = new GoodSpecIndexTask(start, middle, lastTime, currentTime);
            GoodSpecIndexTask right = new GoodSpecIndexTask(middle, end, lastTime, currentTime);
            //两个任务并发执行起来
            invokeAll(left, right);
        }
        return null;
    }

    private void initIndex(int start, int end, long lastTime, long currentTime) {
        List<SpecVO> specVOList = new ArrayList<>();
        if (lastTime == 9999999999999L) {
            log.info("开始创建索引,start:{},end:{}", start, end);
            specVOList = this.getInitSpecList(start, end, null, new Date(currentTime));
        } else {
            specVOList = this.getInitSpecList(start, end, new Date(lastTime), new Date(currentTime));
        }

        this.initSpecIndex(specVOList);

    }

    private boolean initSpecIndex(List<SpecVO> specVOList) {
//        log.info("进入同步索引方法{}", specVOList.size());
        StoreSearchDao storeSearchDao = SpringContextUtils.getBean(StoreSearchDao.class);
        EsDataUtils esDataUtils = SpringContextUtils.getBean(EsDataUtils.class);
        specVOList.stream().forEach(specVO -> {
            GoodsVO goodsVO = specVO.getGoodsVO();
            StoreDTO storeDTO = null;
            if (BeanUtil.isEmpty(goodsVO)) {
                log.error("当前商品未同步规格索引，商品ID为{}", specVO.getGoodsId());
            } else {
                storeDTO = storeSearchDao.get(goodsVO.getStoreId());
            }
            if (storeDTO != null) {
                String gradeName = storeSearchDao.getNameById(storeDTO.getGradeId());
                specVO.getGoodsVO().setStoreGrade(gradeName);
                specVO.getGoodsVO().setStoreLogo(storeDTO.getStoreLogo());
                // 不更新活动es
                specVO.setSpecActivityList(null);
            }
        });
        log.info("开始生成索引{}", specVOList.size());
//        boolean result = esDataUtils.saveDataBatch(IndexContant.GOODS_SPEC_INDEX_NAME, "id", JSON.toJSONString(specVOList), SpecVO.class);
        boolean result = esDataUtils.updateDataBatch(IndexContant.GOODS_SPEC_INDEX_NAME, "id", JSON.toJSONString(specVOList), SpecVO.class);
        return result;
    }

    private List<SpecVO> getInitSpecList(int start, int end, Date lastTime, Date currentTime) {
        GoodsSearchDao goodsSearchDao = SpringContextUtils.getBean(GoodsSearchDao.class);
        int limit = end - start;
//        int page = (start / limit) * limit;
        log.info("传入页码为{}，{}", start, limit);
        List<SpecVO> specVOList = goodsSearchDao.pageListSpec(start, limit, lastTime == null ? null : lastTime, currentTime);
        return specVOList;
    }
}
