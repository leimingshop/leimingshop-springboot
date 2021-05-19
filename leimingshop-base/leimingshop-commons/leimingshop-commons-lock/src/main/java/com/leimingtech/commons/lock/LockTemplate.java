/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.lock;

import com.leimingtech.commons.lock.util.LockUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.UUID;


/**
 * <p>
 * 锁模板方法
 * </p>
 *
 * @author zengzh TaoYu
 */
@Slf4j
public class LockTemplate {

    //    private static final String PROCESS_ID = LockUtil.getLocalMAC() + LockUtil.getJvmPid();
    private static final String PROCESS_ID = UUID.randomUUID().toString() + LockUtil.getJvmPid();

    @Setter
    private LockExecutor lockExecutor;

    public LockInfo lock(String key, long expire, long timeout) throws Exception {
        Assert.isTrue(timeout > 0, "tryTimeout must more than 0");
        long start = System.currentTimeMillis();
        int acquireCount = 0;
        String value = PROCESS_ID + Thread.currentThread().getId();
        while (System.currentTimeMillis() - start < timeout) {
            boolean result = lockExecutor.acquire(key, value, expire);
            acquireCount++;
            if (result) {
                return new LockInfo(key, value, expire, timeout, acquireCount);
            }
            Thread.sleep(50);
        }
        log.info("lock failed, try {} times", acquireCount);
        return null;
    }

    public boolean releaseLock(LockInfo lockInfo) {
        return lockExecutor.releaseLock(lockInfo);
    }
}
