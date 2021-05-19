//package com.leimingtech.modules.index.forkjoin;
//
//import com.alibaba.fastjson.JSON;
//import com.leimingtech.commons.tools.utils.SpringContextUtils;
//import com.leimingtech.modules.constant.IndexContant;
//import com.leimingtech.modules.dao.goods.search.GoodsSearchDao;
//import com.leimingtech.modules.dto.goods.search.GoodsSearchDTO;
//import com.leimingtech.modules.utils.EsDataUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.RecursiveTask;
//
//@Slf4j
//public class GoodIndexTask extends RecursiveTask<Void> {
//
//    // 阈值
//    private static final int limit = 100;
//
//    private int start;
//
//    private int end;
//
//    private long lastTime;
//
//    private long currentTime;
//
//    public GoodIndexTask(int start, int end, long lastTime, long currentTime) {
//        this.start = start;
//        this.end = end;
//        this.lastTime = lastTime;
//        this.currentTime = currentTime;
//    }
//
//    @Override
//    protected Void compute() {
//        if (end - start < limit) {
//            log.info("开始执行{}，{}，{}，{}", start, end, lastTime, currentTime);
//            initIndex(start, end, lastTime, currentTime);
//        } else {
//            //如果当前任务数量超出阈值，则进行任务拆分
//            int middle = (start + end) / 2;
//            GoodIndexTask left = new GoodIndexTask(start, middle, lastTime, currentTime);
//            GoodIndexTask right = new GoodIndexTask(middle, end, lastTime, currentTime);
//            //两个任务并发执行起来
//            invokeAll(left, right);
//        }
//        return null;
//    }
//
//    private void initIndex(int start, int end, long lastTime, long currentTime) {
//        List<GoodsSearchDTO> goodsSearchDTOS = new ArrayList<>();
//        if (lastTime == 9999999999999L) {
//            log.info("开始创建商品索引,start:{},end:{}", start, end);
//            goodsSearchDTOS = this.getInitGoodsList(start, end, null, new Date(currentTime));
//        } else {
//            goodsSearchDTOS = this.getInitGoodsList(start, end, new Date(lastTime), new Date(currentTime));
//        }
//
//        this.initGoodsIndex(goodsSearchDTOS);
//
//    }
//
//    private boolean initGoodsIndex(List<GoodsSearchDTO> goodsSearchDTOS) {
////        log.info("进入同步索引方法{}", goodsSearchDTOS.size());
//
//        GoodsSearchDao goodsSearchDao = SpringContextUtils.getBean(GoodsSearchDao.class);
//        EsDataUtils esDataUtils = SpringContextUtils.getBean(EsDataUtils.class);
//        goodsSearchDTOS.stream().forEach(goodsSearchDTO -> {
//            String avgGoodsEvaluate = goodsSearchDao.reputably(goodsSearchDTO.getId());
//            goodsSearchDTO.setGoodEvaluate(avgGoodsEvaluate);
//            goodsSearchDTO.setGoodsType(Integer.valueOf(goodsSearchDTO.getStoreType()));
//            List<String> keywordTipsList = new ArrayList<>();
//            if (StringUtils.isNotBlank(goodsSearchDTO.getBrandName())) {
//                keywordTipsList.add(goodsSearchDTO.getBrandName());
//            }
//            if (StringUtils.isNotBlank(goodsSearchDTO.getGcName())) {
//                keywordTipsList.add(goodsSearchDTO.getGcName());
//            }
//            goodsSearchDTO.setKeywordTips(keywordTipsList);
//        });
//        log.info("开始生成商品索引{}", goodsSearchDTOS.size());
//
//        boolean result = false;
//        try {
//            result = esDataUtils.updateDataBatch(IndexContant.GOODS_INDEX_NAME, "id", JSON.toJSONString(goodsSearchDTOS), GoodsSearchDTO.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//
//    }
//
//    private List<GoodsSearchDTO> getInitGoodsList(int start, int end, Date lastTime, Date currentTime) {
//        GoodsSearchDao goodsSearchDao = SpringContextUtils.getBean(GoodsSearchDao.class);
//        int limit = end - start;
//        log.info("传入页码为{}，{}", start, limit);
//        List<GoodsSearchDTO> goodsSearchDTOList = goodsSearchDao.pageListGoods(start, limit, lastTime == null ? null : lastTime, currentTime);
//        return goodsSearchDTOList;
//    }
//}
