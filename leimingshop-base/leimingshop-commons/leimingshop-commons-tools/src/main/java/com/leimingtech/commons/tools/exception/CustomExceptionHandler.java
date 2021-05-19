/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.exception;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.config.ModuleConfig;
import com.leimingtech.commons.tools.log.SysLogError;
import com.leimingtech.commons.tools.log.enums.LogTypeEnum;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


/**
 * 异常处理器
 *
 * @since 1.0.0
 */
@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
    @Autowired
    private ModuleConfig moduleConfig;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public Result handleCustomException(CustomException ex) {
        Result result = new Result();
        result.error(ex.getCode(), ex.getMsg());
        saveLog(ex);
        return result;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        Result result = new Result();
        result.error(ErrorCode.DB_RECORD_EXISTS);
        saveLog(ex);
        return result;
    }

    /**
     * Feign服务端异常处理，
     * Controller不建议使用 throw new ServiceException()
     *
     * @date 2020/6/12 20:05
     * @author lixiangx@leimingtech.com
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException ex) {
        Result result = new Result();
        result.error(ex.getErrorCode().getCode(), ex.getMessage());
        saveLog(ex);
        return result;
    }


    /**
     * SentinelBadRequestException 异常不会进入fallback
     *
     * @date 2020/6/12 20:05
     * @author lixiangx@leimingtech.com
     */
    @ExceptionHandler(SentinelBadRequestException.class)
    public Result handleHystrixBadRequestException(SentinelBadRequestException ex) {
        if (ex.getCause() instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) ex.getCause();
            return new Result().error(505, serviceException.getErrorCode().getCode(), serviceException.getMessage());
        }
        return new Result().error();
    }


//    @ExceptionHandler(Exception.class)
//    public Result handleException(Exception ex) {
//        logger.error(ex.getMessage(), ex);
//
//        saveLog(ex);
//
//        return new Result().error();
//    }

    /**
     * 保存异常日志
     */
    private void saveLog(Exception ex) {
        SysLogError log = new SysLogError();
        log.setType(LogTypeEnum.ERROR.value());
        log.setModule(moduleConfig.getName());

        //请求相关信息
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod());
        log.setIp(IpUtils.getIpAddr(request));
        Map<String, String> params = HttpContextUtils.getParameterMap(request);
        if (MapUtil.isNotEmpty(params)) {
            log.setRequestParams(JSON.toJSONString(params));
        }
        //登录用户ID
        //log.setCreator(SecurityUser.getUser().getUsername());
        //异常信息
        log.setErrorInfo(ExceptionUtils.getErrorStackTrace(ex));

        //保存到Redis队列里
        log.setCreateDate(new Date());
        //使用RabbitMQ发送消息进行日志处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ADMIN_ERROR_LOG_QUEUE,
                JSON.toJSONString(log));
    }
}
