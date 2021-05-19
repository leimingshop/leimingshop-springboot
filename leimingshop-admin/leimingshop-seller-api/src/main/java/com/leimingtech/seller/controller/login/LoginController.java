/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.login;


import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.log.StoreLogLogin;
import com.leimingtech.commons.tools.log.enums.LogTypeEnum;
import com.leimingtech.commons.tools.log.enums.LoginOperationEnum;
import com.leimingtech.commons.tools.log.enums.LoginStatusEnum;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.redis.SellerDetailRedis;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.dto.member.AuthorizationDTO;
import com.leimingtech.modules.dto.member.LoginDTO;
import com.leimingtech.modules.dto.member.UpdateLoginDTO;
import com.leimingtech.modules.dto.user.StoreUserDTO;
import com.leimingtech.modules.dto.user.StoreUserPhoneEmailDTO;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.member.CaptchaCodeService;
import com.leimingtech.modules.service.resource.StoreResourceService;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.seller.constant.SellerConstants;
import com.leimingtech.seller.jwt.JwtProperties;
import com.leimingtech.seller.jwt.JwtUtils;
import com.leimingtech.seller.service.CaptchaService;
import com.leimingtech.seller.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/22 4:37 PM
 */
@Slf4j
@RestController
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private SellerDetailRedis sellerDetailRedis;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private StoreResourceService storeResourceService;

    @Autowired
    private CaptchaCodeService captchaCodeService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("generate/image")
    @ApiOperation(value = "获取图片验证码", produces = "application/octet-stream")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "uuid", required = true)
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        //uuid不能为空
        AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);

        //生成验证码
        captchaService.create(response, SellerConstants.APPLICATION_NAME + "-" + uuid);
    }


    /**
     * seller端用户登陆接口
     *
     * @param login: 用户登陆信息
     * @return 用户授权信息
     * @date 2019/6/26 14:41
     * @author lixiang
     **/
    @PostMapping("login")
    @ApiOperation(value = "登录")
    public Result<AuthorizationDTO> login(@RequestBody LoginDTO login) {
        //效验数据
        ValidatorUtils.validateEntity(login);
        //验证码是否正确
        boolean flag = captchaService.validate(SellerConstants.APPLICATION_NAME + "-" + login.getUuid(), login.getCaptcha());
        if (!flag) {
            return new Result<AuthorizationDTO>().error(ErrorCode.CAPTCHA_ERROR);
        }

        // 密码解密
        // 使用RSA私钥进行解密
        try {
            login.setPassword(RSACoder.decryptByPrivateKey(login.getPassword()));
            login.setUsername(RSACoder.decryptByPrivateKey(login.getUsername()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", login.getUsername(), e);
            return new Result().error("密码解析失败，请重新输入");
        }
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //获取用户信息
        SellerDetail sellerDetail = storeUserService.login(login.getUsername());

        //账号不存在
        if (sellerDetail == null) {
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        if (sellerDetail.getIsEnable() == 1) {
            throw new CustomException("该账号已禁用，请联系平台管理人员");
        }

        //登陆日志
        StoreLogLogin log = new StoreLogLogin();
        if (sellerDetail.getStoreId() != null) {
            log.setType(LogTypeEnum.LOGIN.value());
            log.setStoreId(sellerDetail.getStoreId());
            log.setOperation(LoginOperationEnum.LOGIN.value());
            log.setCreateDate(new Date());
            log.setIp(IpUtils.getIpAddr(request));
            log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        }

        //密码错误
        if (!PasswordUtils.matches(login.getPassword(), sellerDetail.getPassword())) {
            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreator(login.getUsername());
            //使用RabbitMQ发送消息进行日志处理
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SELLER_LOGIN_LOG_QUEUE,
                    JSON.toJSONString(log));
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }

        //TODO lixiang 后期需要增加账号是否停用判断
        List<ResourceBO> userResourceList = storeResourceService.getUserResourceList(sellerDetail.getId());
        sellerDetail.setResourceList(userResourceList);

        //将用户信息保存至Redis
        sellerDetailRedis.set(sellerDetail, jwtProperties.getExpire());

        //登录成功，生成token
        String token = jwtUtils.generateToken(sellerDetail.getId());

        //授权信息
        AuthorizationDTO authorization = new AuthorizationDTO();
        authorization.setToken(token);
        authorization.setExpire(jwtUtils.getExpire());

        // /登录用户信息
        log.setCreator(sellerDetail.getAccount());
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        //使用RabbitMQ发送消息进行日志处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SELLER_LOGIN_LOG_QUEUE,
                JSON.toJSONString(log));
        return new Result<AuthorizationDTO>().ok(authorization, "登录成功");
    }

    /**
     * 用户退出
     *
     * @date 2019/6/26 15:21
     * @author lixiang
     **/
    @PostMapping(value = "logout")
    @ApiOperation(value = "退出")
    public Result logout() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // request中获取sellerId
        Object sellerIdObj = request.getAttribute(SellerConstants.SELLER_USER_KEY);
        if (null == sellerIdObj) {
            return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "退出失败");
        }
        //根据id获取用户信息
        SellerDetail sellerDetail = sellerDetailRedis.get(Long.parseLong(sellerIdObj.toString()));
        //退出日志
        StoreLogLogin log = new StoreLogLogin();
        if (sellerDetail != null) {
            log.setType(LogTypeEnum.LOGIN.value());
            log.setStoreId(sellerDetail.getStoreId());
            log.setOperation(LoginOperationEnum.LOGOUT.value());
            log.setIp(IpUtils.getIpAddr(request));
            log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
            log.setIp(IpUtils.getIpAddr(request));
            log.setStatus(LoginStatusEnum.SUCCESS.value());
            log.setCreator(sellerDetail.getAccount());
            log.setCreateDate(new Date());
        }
        //使用RabbitMQ发送消息进行日志处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SELLER_LOGIN_LOG_QUEUE,
                JSON.toJSONString(log));

        sellerDetailRedis.logout(Long.valueOf(sellerIdObj.toString()));
        return new Result().ok(null, "退出成功");
    }

    /**
     * 修改密码
     *
     * @param updateLoginDTO 参数
     * @return
     */
    @PutMapping("update/password")
    @ApiOperation(value = "修改密码")
    public Result updatePassword(@RequestBody UpdateLoginDTO updateLoginDTO) {
        //效验数据
        ValidatorUtils.validateEntity(updateLoginDTO);

        // 密码解密
        // 使用RSA私钥进行解密
        try {
            updateLoginDTO.setNewPassword(RSACoder.decryptByPrivateKey(updateLoginDTO.getNewPassword()));
            updateLoginDTO.setUsername(RSACoder.decryptByPrivateKey(updateLoginDTO.getUsername()));
            updateLoginDTO.setConfirmPassword(RSACoder.decryptByPrivateKey(updateLoginDTO.getConfirmPassword()));
            updateLoginDTO.setOldPassword(RSACoder.decryptByPrivateKey(updateLoginDTO.getOldPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", updateLoginDTO.getUsername(), e);
            return new Result().error("密码解析失败，请重新输入");
        }

        //获取用户信息
        SellerDetail sellerDetail = storeUserService.login(updateLoginDTO.getUsername());

        if (sellerDetail == null) {
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        if (sellerDetail.getIsEnable() == 1) {
            return new Result().error("账户被禁用，请联系管理员");
        }

        //密码错误
        if (!PasswordUtils.matches(updateLoginDTO.getOldPassword(), sellerDetail.getPassword())) {
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        // 验证新密码
        if (!updateLoginDTO.getNewPassword().equals(updateLoginDTO.getConfirmPassword())) {
            return new Result().error("两次输入密码不一致");
        }

        if (!updateLoginDTO.getUsername().matches(RegexConstant.SAVE_UPDATE_ACCOUNT_REGEX)) {
            return new Result().error("账号必须为6-20位字符，数字，下划线");
        }
        if (!updateLoginDTO.getNewPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }

        sellerDetail.setPassword(PasswordUtils.encode(updateLoginDTO.getConfirmPassword()));
        StoreUserDTO storeUserDTO = ConvertUtils.sourceToTarget(sellerDetail, StoreUserDTO.class);
        storeUserService.update(storeUserDTO);
        return new Result().ok(null, "修改成功");
    }


    /**
     * 获取seller资源文件
     *
     * @param token:  jwt-token
     * @param url:    请求URL
     * @param method: GET/POST
     * @return 资源文件
     * @date 2019/6/25 18:03
     * @author lixiang
     **/
    @PostMapping("seller/resource")
    public Result<SellerDetail> sellerResource(@RequestParam(value = "token", required = false) String token,
                                               @RequestParam("url") String url,
                                               @RequestParam("method") String method) {

        SellerDetail sellerDetail = resourceService.resource(token, url, method);

        return new Result<SellerDetail>().ok(sellerDetail);
    }

    /**
     * 校验图形验证码
     *
     * @return
     */
    @GetMapping("verify/captcha")
    @ApiOperation(value = "校验图形验证码")
    public Result forGetPWD(@RequestParam("uuid") String uuid, @RequestParam("captcha") String captcha) {
        // 验证码校验
        boolean flag = captchaService.validate(SellerConstants.APPLICATION_NAME + "-" + uuid, captcha);
        if (!flag) {
            return new Result<AuthorizationDTO>().error(ErrorCode.CAPTCHA_ERROR);
        }
        return new Result().ok(null, "校验通过");
    }

    /**
     * 获取手机或邮箱验证码
     *
     * @param verifyType 验证方式 1 手机号 2 邮箱
     * @param linkman
     * @return
     */
    @GetMapping("phone/captcha")
    @ApiOperation(value = "获取手机或邮箱验证码")
    public Result phoneCaptcha(@RequestParam("verifyType") Integer verifyType, @RequestParam("linkman") String linkman) {
        if (verifyType == 1) {
            captchaCodeService.moblieCaptcha(linkman, MemberRedisConstants.STORE_FORGET_PWD_PREFIX);
        } else {
            captchaCodeService.emailCaptha(linkman, MemberEnum.SEND_TYPE_FORGET_PWD.value());
        }
        return new Result().ok(null, "发送成功");
    }

    /**
     * 校验手机号邮箱验证码是否正确
     *
     * @param verifyType 1 手机号 2 邮箱
     * @param linkman
     * @return
     */
    @GetMapping("verify/phone/captcha")
    @ApiOperation(value = "校验手机号邮箱验证码是否正确")
    public Result verifyPhoneCaptcha(@RequestParam("verifyType") Integer verifyType,
                                     @RequestParam("linkman") String linkman,
                                     @RequestParam("captcha") String captcha) {
        if (verifyType == 1) {
            String phoneCaptcha = (String) redisUtils.get(MemberRedisConstants.STORE_FORGET_PWD_PREFIX + linkman);
            if (StringUtils.isBlank(phoneCaptcha)) {
                return new Result().error(MemberErrorEnum.E_CAPTCHA_PASS.code(), MemberErrorEnum.E_CAPTCHA_PASS.value());
            }

            if (!StringUtils.equals(phoneCaptcha, captcha)) {
                return new Result().error(MemberErrorEnum.E_CAPTCHA_ERROR.code(), MemberErrorEnum.E_CAPTCHA_ERROR.value());
            }

        } else {
            String emailCaptcha = String.valueOf(redisUtils.get(linkman));
            if (StringUtils.isBlank(emailCaptcha)) {
                return new Result().error(MemberErrorEnum.E_CAPTCHA_PASS.code(), MemberErrorEnum.E_CAPTCHA_PASS.value());
            }

            if (!StringUtils.equals(emailCaptcha, captcha)) {
                return new Result().error(MemberErrorEnum.E_CAPTCHA_ERROR.code(), MemberErrorEnum.E_CAPTCHA_ERROR.value());
            }
        }

        return new Result().ok(null, "验证通过");

    }


    /**
     * 校验账号是否存在
     *
     * @param userName
     * @return
     */
    @GetMapping("verify/username")
    @ApiOperation("校验账号是否存在")
    @LogOperation(value = "校验账号是否存在")
    public Result verifyUsername(@RequestParam String userName) {

        Integer verify = storeUserService.verifyUserPhone(userName);
        if (verify <= 0) {
            return new Result().error("该用户名/手机号/邮箱不存在，请重新输入");
        }
        StoreUserPhoneEmailDTO phoneEmail = storeUserService.getPhoneEmail(userName);

        return new Result().ok(phoneEmail, "账号可用");
    }

    /**
     * 忘记密码
     */
    @PutMapping("forget/password")
    @ApiOperation("忘记密码")
    @LogOperation("忘记密码")
    public Result forgetPWD(@RequestBody UpdateLoginDTO updateLoginDTO) {


        //使用RSA私钥进行解密
        try {
            updateLoginDTO.setNewPassword(RSACoder.decryptByPrivateKey(updateLoginDTO.getNewPassword()));
            updateLoginDTO.setUsername(RSACoder.decryptByPrivateKey(updateLoginDTO.getUsername()));
            updateLoginDTO.setConfirmPassword(RSACoder.decryptByPrivateKey(updateLoginDTO.getConfirmPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", updateLoginDTO.getUsername(), e);
            return new Result().error("密码解析失败，请重新输入");
        }
        // 获取用户信息
        StoreUserPhoneEmailDTO phoneEmail = storeUserService.getPhoneEmail(updateLoginDTO.getUsername());

        if (phoneEmail == null) {
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }

        // 验证新密码
        if (!updateLoginDTO.getNewPassword().equals(updateLoginDTO.getConfirmPassword())) {
            return new Result().error("两次输入密码不一致");
        }

        // 校验密码长度
        if (!updateLoginDTO.getNewPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }
        StoreUserDTO storeUserDTO = new StoreUserDTO();
        storeUserDTO.setPassword(PasswordUtils.encode(updateLoginDTO.getConfirmPassword()));
        storeUserDTO.setId(phoneEmail.getId());
        storeUserService.update(storeUserDTO);


        return new Result().ok(null, "修改成功");

    }
}
