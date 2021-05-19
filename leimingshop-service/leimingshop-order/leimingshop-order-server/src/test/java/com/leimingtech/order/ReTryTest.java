/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 测试Spring-retry重试机制
 * @Date: 14:05 2019/6/27
 * @Version: V1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReTryTest {


    @Test
    public void test() {
        retry();
    }


    @Retryable(value = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000L, multiplier = 2))
    public void retry() {
        log.error("Call something...");
        throw new RuntimeException("RPC调用异常");
    }

    /**
     * recover 机制
     *
     * @param e 异常
     */
    @Recover
    public void recover(RuntimeException e) {
        log.info("Start do recover things....");
        log.warn("We meet ex: ", e);
    }


}
