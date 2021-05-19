/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogContextParam;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/6/29 18:54
 * @Version: V1.0
 */
@Slf4j
public class SdkLogUtils {

    private static Logger logger = LoggerFactory.getLogger(SdkLogUtils.class);

    private SdkLogUtils() {
    }

    /**
     * 处理切点
     *
     * @param joinPoint
     **/
    public static void handlerJoinPoint(JoinPoint joinPoint) {
        if (null == joinPoint) {
            return;
        }
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            logger.error("类找不到异常" + e);
        }
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    if (!method.isAnnotationPresent(LogContext.class)) {
                        return;
                    }
                    ThreadLocalUtils.logContextThreadLocal.set(method.getAnnotation(LogContext.class));
                    break;
                }
            }
        }


        //获取上下文
        LogContext logContext = ThreadLocalUtils.logContextThreadLocal.get();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        String methodNameGet = methodSignature.getMethod().getName();
        log.info("切入点实例:{}", joinPoint.getSignature().getDeclaringTypeName());
        //获取参数名称和值
        String[] parameterNames = methodSignature.getParameterNames();
        Map<String, String> logContextMap = new HashMap<>();
        if (null != parameterNames && parameterNames.length > 0) {
            for (int i = 0; i < parameterNames.length; i++) {
                logContextMap.put(parameterNames[i], args[i] != null ? args[i].toString() : "");
            }
        }
        if (ArrayUtils.isEmpty(parameterNames)
                && ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                if (ArrayUtils.isNotEmpty(parameterAnnotations)) {
                    log.info("参数注解长度: {}", parameterAnnotations.length);
                    try {
                        LogContextParam logServiceParam = (LogContextParam) parameterAnnotations[i][0];
                        logContextMap.put(logServiceParam.name(), joinPoint.getArgs()[i] != null ? joinPoint.getArgs()[i].toString() : "");
                    } catch (Exception e) {
                        logger.error("错误信息为" + e);
                        //参数注解二维数组获取失败时
                        logContextMap.put("param" + (i + 1), joinPoint.getArgs()[i] != null ? joinPoint.getArgs()[i].toString() : "");
                    }
                } else {
                    logContextMap.put("param" + (i + 1), joinPoint.getArgs()[i] != null ? joinPoint.getArgs()[i].toString() : "");
                }
            }
        }
        if (null != logContext) {
            //声明切入点监控日志
            MonitorLogger monitorLogger = MonitorLoggerFactory.getMonitorLogger(methodSignature.getDeclaringType());
            log.info("被调用方法{}入参输出......", methodNameGet);
            monitorLogger.info(logContext.code(), logContext.note(), logContextMap);
            //移除完成后的线程本地变量
            ThreadLocalUtils.logContextServiceParamThreadLocal.remove();
            ThreadLocalUtils.logContextThreadLocal.remove();
        }
    }

    /**
     * 转换
     *
     * @param t
     * @param logContext
     **/
    public static <T> void convert(T t, Map<String, String> logContext) {
        Class<?> superClazz = t.getClass().getSuperclass();
        Class<?> clazz = t.getClass();
        Field[] superFields = superClazz.getDeclaredFields();
        Field[] fields = clazz.getDeclaredFields();
        List<Field> newFieldList = new ArrayList<>();
        for (int i = 0; i < superFields.length; i++) {
            newFieldList.add(superFields[i]);
        }
        for (int i = 0; i < fields.length; i++) {
            newFieldList.add(fields[i]);
        }
        newFieldList.forEach(field -> {
            try {
                field.setAccessible(true);
                logContext.put(field.getName(), field.get(t) != null ? String.valueOf(field.get(t)) : null);
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                log.error("转换异常:{0}", e);
            }
        });
    }
}
