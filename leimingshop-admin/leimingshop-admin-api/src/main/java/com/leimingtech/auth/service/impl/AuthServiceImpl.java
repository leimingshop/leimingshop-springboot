/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.leimingtech.auth.dto.AuthorizationDTO;
import com.leimingtech.auth.dto.LoginDTO;
import com.leimingtech.auth.jwt.JwtProperties;
import com.leimingtech.auth.jwt.JwtUtils;
import com.leimingtech.auth.service.AuthService;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.log.SysLogLogin;
import com.leimingtech.commons.tools.log.enums.LogTypeEnum;
import com.leimingtech.commons.tools.log.enums.LoginOperationEnum;
import com.leimingtech.commons.tools.log.enums.LoginStatusEnum;
import com.leimingtech.commons.tools.redis.UserDetailRedis;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.dto.sys.SysUserDTO;
import com.leimingtech.enums.UserStatusEnum;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.sys.SysResourceService;
import com.leimingtech.service.sys.SysRoleDataScopeService;
import com.leimingtech.service.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * 认证服务
 *
 * @since 1.0.0
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDetailRedis userDetailRedis;

    @Autowired
    private SysRoleDataScopeService sysRoleDataScopeService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public AuthorizationDTO login(LoginDTO login) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        //获取用户信息
        SysUserDTO sysUserDTO = sysUserService.getByUsername(login.getUsername());

        //登录日志
        SysLogLogin sysLogLogin = new SysLogLogin();
        sysLogLogin.setType(LogTypeEnum.LOGIN.value());
        sysLogLogin.setOperation(LoginOperationEnum.LOGIN.value());
        sysLogLogin.setCreateDate(new Date());
        sysLogLogin.setIp(IpUtils.getIpAddr(request));
        sysLogLogin.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));

        //账号不存在
        if (sysUserDTO == null) {
            sysLogLogin.setStatus(LoginStatusEnum.FAIL.value());
            sysLogLogin.setCreator(login.getUsername());
            //使用RabbitMQ发送消息进行日志处理
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE,
                    JSON.toJSONString(sysLogLogin));
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }


        //密码错误
        if (!PasswordUtils.matches(login.getPassword(), sysUserDTO.getPassword())) {
            sysLogLogin.setStatus(LoginStatusEnum.FAIL.value());
            sysLogLogin.setCreator(sysUserDTO.getUsername());

            //使用RabbitMQ发送消息进行日志处理
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE,
                    JSON.toJSONString(sysLogLogin));
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }

        //账号停用
        if (sysUserDTO.getStatus() == UserStatusEnum.DISABLE.value()) {
            sysLogLogin.setStatus(LoginStatusEnum.LOCK.value());
            sysLogLogin.setCreator(sysUserDTO.getUsername());
            //使用RabbitMQ发送消息进行日志处理
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE,
                    JSON.toJSONString(sysLogLogin));
            throw new CustomException(ErrorCode.ACCOUNT_DISABLE);
        }

        UserDetail user = ConvertUtils.sourceToTarget(sysUserDTO, UserDetail.class);

        //用户部门数据权限
        List<Long> deptIdList = sysRoleDataScopeService.getDataScopeList(user.getId());
        user.setDeptIdList(deptIdList);

        //用户资源列表
        List<ResourceBO> resourceList = sysResourceService.getUserResourceList(user.getId());
        user.setResourceList(resourceList);

        //保存到Redis
        userDetailRedis.set(user, jwtProperties.getExpire());

        //登录成功，生成token
        String token = jwtUtils.generateToken(sysUserDTO.getId());

        //授权信息
        AuthorizationDTO authorization = new AuthorizationDTO();
        authorization.setToken(token);
        authorization.setExpire(jwtProperties.getExpire());

        //登录用户信息
        sysLogLogin.setCreator(sysUserDTO.getUsername());
        sysLogLogin.setStatus(LoginStatusEnum.SUCCESS.value());
        //使用RabbitMQ发送消息进行日志处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE,
                JSON.toJSONString(sysLogLogin));
        return authorization;
    }

    @Override
    public void logout(Long userId) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        UserDetail user = SecurityUser.getUser();

        //退出日志
        SysLogLogin log = new SysLogLogin();
        log.setType(LogTypeEnum.LOGIN.value());
        log.setOperation(LoginOperationEnum.LOGOUT.value());
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setIp(IpUtils.getIpAddr(request));
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreator(user.getUsername());
        log.setCreateDate(new Date());
        //使用RabbitMQ发送消息进行日志处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE,
                JSON.toJSONString(log));

        userDetailRedis.logout(userId);
    }

}
