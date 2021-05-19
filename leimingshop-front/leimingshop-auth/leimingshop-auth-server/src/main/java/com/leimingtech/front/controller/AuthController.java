/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.controller;

import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.front.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权管理
 * <p>
 * 刷新token 使用OAuth2默认接口地址接口地址
 * 刷新令牌需要四个参数：
 * grant_type：值必须是 refresh_token
 * client_id:client
 * client_secret:secret
 * refresh_token ： 上次获取令牌时回参中的 refresh_token
 * POST http://IP:port/auth/oauth/token
 *
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("auth")
@Api(tags = "登录管理")
public class AuthController {

    @Resource
    private CaptchaService captchaService;

    @PostMapping("oauth/gettoken")
    @ApiOperation(value = "用户名密码登录接口")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "username", required = true
    )
    public void gettoken(HttpServletResponse response, @RequestParam(value = "password", required = true) String password,
                         @RequestParam(value = "username", required = true) String username,
                         @RequestParam(value = "token", required = true) String token,
                         @RequestParam(value = "X", required = true) String X,
                         @RequestParam(value = "Y", required = true) String Y
    ) throws IOException {
        log.info("获取[{}]用户Token:", username);
    }


    @PostMapping("oauth/captcha")
    @ApiOperation(value = "登录接口（图形验证码）")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", required = true
    )
    public void getCaptchaToken(HttpServletResponse response, @RequestParam(value = "password", required = true) String password,
                                @RequestParam(value = "mobile", required = true) String username,
                                @RequestParam(value = "uuid", required = true) String uuid,
                                @RequestParam(value = "captcha", required = true) String captcha
    ) throws IOException {
        log.info("获取[{}]登录接口（图形验证码)Token:", username);
    }

    @PostMapping("authentication/mobile")
    @ApiOperation(value = "手机验证码登录接口")
    public void mobile(HttpServletResponse response, @RequestParam(value = "mobile", required = true) String mobile,
                       @RequestParam(value = "code", required = true) String code
    ) throws IOException {
        log.info("获取[{}]用户Token:", mobile);
    }

    @GetMapping("logout")
    @ApiOperation(value = "退出")
    public void logout(HttpServletResponse response, HttpServletRequest request,
                       @RequestParam(value = "access_token", required = true) String access_token
    ) throws IOException {

    }

    @PostMapping("wechat/login")
    @ApiOperation(value = "微信h5登录获取token/微信登录绑定手机号则手机号验证码必传/微信小程序登录则type必传且为miniwx")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信code", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "绑定手机号", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "validCode", value = "短信验证码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "第三方登录类型 wechat、miniwx(微信小程序登录必传miniwx)", paramType = "query", dataType = "string")
    })
    public void wechatLogin(HttpServletResponse response, @RequestParam("code") String code, @RequestParam(value = "mobile") String mobile, @RequestParam(value = "validCode") String validCode, @RequestParam(value = "type") int type) {
        log.info("获取微信unionid[{}]用户Token:", code);
    }

    @PostMapping("wechat/pc/login")
    @ApiOperation(value = "微信PC登录获取token/微信PC登录绑定手机号则手机号验证码必传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信code", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "绑定手机号", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "validCode", value = "短信验证码", paramType = "query", dataType = "string")
    })
    public void wechatPcLogin(HttpServletResponse response, @RequestParam("code") String code, @RequestParam(value = "mobile") String mobile, @RequestParam(value = "validCode") String validCode) {
        log.info("获取微信unionid[{}]用户Token:", code);
    }

    @PostMapping("wechat/app/login")
    @ApiOperation(value = "微信app登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unionId", value = "unionId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "openId", value = "openId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "gender", value = "性别", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "iconurl", value = "头像", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "screen_name", value = "昵称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "绑定手机号", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "validCode", value = "短信验证码", paramType = "query", dataType = "string")
    })
    public void wechatLogin(HttpServletResponse response, @RequestParam("unionId") String unionId, @RequestParam("openId") String openId,
                            @RequestParam("gender") String gender, @RequestParam("iconurl") String iconurl,
                            @RequestParam("screen_name") String screen_name, @RequestParam(value = "mobile") String mobile,
                            @RequestParam(value = "validCode") String validCode) {
        log.info("获取微信unionid[{}]用户Token:", unionId);
    }

    @PostMapping("weibo/login")
    @ApiOperation(value = "微博登录获取token/微博登录绑定手机号则手机号验证码必传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微博code", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "绑定手机号", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "validCode", value = "短信验证码", paramType = "query", dataType = "string")
    })
    public void weiboLogin(HttpServletResponse response, @RequestParam("code") String code, @RequestParam(value = "mobile") String mobile, @RequestParam(value = "validCode") String validCode) {
        log.info("获取微博unionid[{}]用户Token:", code);
    }


    @GetMapping("captcha")
    @ApiOperation(value = "验证码", produces = "application/octet-stream")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "uuid", required = true)
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        //uuid不能为空
        AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);

        //生成图片验证码
        captchaService.create(response, uuid);
    }


}
