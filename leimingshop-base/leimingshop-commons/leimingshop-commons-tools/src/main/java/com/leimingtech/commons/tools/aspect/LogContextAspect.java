/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.aspect;

import com.leimingtech.commons.tools.utils.SdkLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/7/1 10:53
 * @Version: V1.0
 */
@Slf4j
@Aspect
@Component
public class LogContextAspect {

    @Pointcut("@annotation(com.leimingtech.commons.tools.annotation.LogContext)")
    public void sdkLog() {
    }

    @Before(value = "sdkLog()")
    public void sdkLogBefore(JoinPoint joinPoint) {
        SdkLogUtils.handlerJoinPoint(joinPoint);
    }
}
