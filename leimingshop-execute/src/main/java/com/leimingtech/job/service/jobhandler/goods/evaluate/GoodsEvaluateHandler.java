/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.goods.evaluate;

import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Data: 2019/7/25 14:17
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@Component
public class GoodsEvaluateHandler implements Job {


    @Autowired
    private EvaluateGoodsService evaluateGoodsService;


    @Override
    public void run(String params) {
        evaluateGoodsService.saveEvaluateTime();

        log.info("执行商品评价定时任务");

    }
}
