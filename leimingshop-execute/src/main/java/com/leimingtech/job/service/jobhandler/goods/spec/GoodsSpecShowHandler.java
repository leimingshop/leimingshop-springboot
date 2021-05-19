/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.goods.spec;

import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName GoodsSpecShowHandler
 * @Description 规格批量上下架
 * @Author DY
 * @Date 2019/6/14 9:42
 * @Version 1.0
 **/
@Slf4j
@Component
public class GoodsSpecShowHandler implements Job {

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    @Override
    public void run(String params) {
        //上下架规格
        List<Long> goodsIds = goodsSpecService.updateShowTiming();
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            goodsSyncIndexService.batchGoodsIndexSync(goodsIds);
        }
        log.info("执行规格上下架定时任务");

    }
}
