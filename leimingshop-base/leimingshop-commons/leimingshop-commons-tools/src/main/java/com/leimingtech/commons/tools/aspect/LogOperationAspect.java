/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.aspect;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.log.SysLogOperation;
import com.leimingtech.commons.tools.log.enums.LogTypeEnum;
import com.leimingtech.commons.tools.log.enums.OperationStatusEnum;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作日志，切面处理类
 *
 * @since 1.0.0
 */
@Aspect
@Component
@Slf4j
public class LogOperationAspect {

    private static Logger logger = LoggerFactory.getLogger(LogOperationAspect.class);

//    @Autowired
//    private ModuleConfig moduleConfig;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Pointcut("@annotation(com.leimingtech.commons.tools.annotation.LogOperation)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        try {
            //执行方法
            Object result = point.proceed();

            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, OperationStatusEnum.SUCCESS.value());
            return result;
        } catch (Exception e) {
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, OperationStatusEnum.FAIL.value());
            logger.error("错误信息为" + e);
            throw e;
        }
    }


    private void saveLog(ProceedingJoinPoint joinPoint, long time, Integer status) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogOperation log = new SysLogOperation();
        LogOperation annotation = method.getAnnotation(LogOperation.class);
        if (annotation != null) {
            //注解上的描述
            log.setOperation(annotation.value());
        }

        //登录用户信息
        UserDetail user = SecurityUser.getUser();
        if (user != null) {
            log.setCreator(user.getUsername());
        }

        log.setType(LogTypeEnum.OPERATION.value());
//        log.setModule(moduleConfig.getName());
        log.setStatus(status);
        log.setRequestTime((int) time);
        log.setCreateDate(new Date());

        //请求相关信息
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod());

        //请求参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSON.toJSONString(args[0]);
            log.setRequestParams(params);
        } catch (Exception e) {
            logger.error("错误信息为" + e);
        }
        //使用RabbitMQ发送消息进行日志处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ADMIN_OPERATION_LOG_QUEUE,
                JSON.toJSONString(log));

    }
}
