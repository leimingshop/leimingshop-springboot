/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.goods;

import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName GoodsShowHandler
 * @Description 商品发布以及上下架
 * @Author DY
 * @Date 2019/6/13 15:47
 * @Version 1.0
 **/
@Slf4j
@Component
public class GoodsShowHandler implements Job {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    @Override
    public void run(String params) {
        //上下架商品
        List<Long> goodsIds = goodsService.updateShowTiming();
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            goodsSyncIndexService.batchGoodsIndexSync(goodsIds);
        }

        //打印在xxl-job-admin控制台
        log.info("执行商品上下架/发布定时任务");
        //打印在服务器控制台
        log.info("执行商品上下架/发布定时任务");
    }
}
