/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.lock.aop;

import com.leimingtech.commons.lock.LockInfo;
import com.leimingtech.commons.lock.LockKeyGenerator;
import com.leimingtech.commons.lock.LockTemplate;
import com.leimingtech.commons.lock.annotation.Lock4j;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 分布式锁aop处理器
 *
 * @author zengzh TaoYu
 */
@Slf4j
public class LockInterceptor implements MethodInterceptor {
    @Setter
    private LockTemplate lockTemplate;

    private LockKeyGenerator lockKeyGenerator = new LockKeyGenerator();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        LockInfo lockInfo = null;
        try {
            Lock4j lock4j = invocation.getMethod().getAnnotation(Lock4j.class);
            String keyName = lockKeyGenerator.getKeyName(invocation, lock4j);
            lockInfo = lockTemplate.lock(keyName, lock4j.expire(), lock4j.timeout());
            if (null != lockInfo) {
                return invocation.proceed();
            }
            throw new RuntimeException();
        } finally {
            if (null != lockInfo) {
                lockTemplate.releaseLock(lockInfo);
            }
        }
    }

}
