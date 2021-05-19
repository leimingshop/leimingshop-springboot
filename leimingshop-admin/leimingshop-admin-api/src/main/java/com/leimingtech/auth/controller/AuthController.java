/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.auth.controller;

import com.leimingtech.auth.dto.AuthorizationDTO;
import com.leimingtech.auth.dto.LoginDTO;
import com.leimingtech.auth.service.AuthService;
import com.leimingtech.auth.service.CaptchaService;
import com.leimingtech.auth.service.ResourceService;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权管理
 *
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "授权管理")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CaptchaService captchaService;

    @GetMapping("captcha")
    @ApiOperation(value = "验证码", produces = "application/octet-stream")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "uuid", required = true)
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        //uuid不能为空
        AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);

        //生成验证码
        captchaService.create(response, uuid);
    }

    @PostMapping(value = "login")
    @ApiOperation(value = "登录")
    public Result<AuthorizationDTO> login(@RequestBody LoginDTO login) {
        //效验数据
        ValidatorUtils.validateEntity(login);

        // 密码解密
        // 使用RSA私钥进行解密
        try {
            login.setPassword(RSACoder.decryptByPrivateKey(login.getPassword()));
            login.setUsername(RSACoder.decryptByPrivateKey(login.getUsername()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", login.getUsername(), e);
            return new Result<AuthorizationDTO>().error("用户名密码错误");
        }

        //验证码是否正确
        boolean flag = captchaService.validate(login.getUuid(), login.getCaptcha());
        if (!flag) {
            return new Result<AuthorizationDTO>().error(ErrorCode.CAPTCHA_ERROR);
        }

        //获取登录授权信息
        AuthorizationDTO authorization = authService.login(login);

        return new Result<AuthorizationDTO>().ok(authorization);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "退出")
    public Result logout(HttpServletRequest request) {
        String userId = (String) request.getAttribute(Constant.USER_KEY);

        authService.logout(Long.parseLong(userId));

        return new Result();
    }

    /**
     * 是否有资源访问权限
     *
     * @param token  token
     * @param url    资源URL
     * @param method 请求方式
     * @return 有访问权限，则返回用户信息
     */
    @PostMapping("resource")
    public Result<UserDetail> resource(@RequestParam(value = "token", required = false) String token,
                                       @RequestParam("url") String url, @RequestParam("method") String method) {
        UserDetail data = resourceService.resource(token, url, method);

        return new Result<UserDetail>().ok(data);
    }
}
