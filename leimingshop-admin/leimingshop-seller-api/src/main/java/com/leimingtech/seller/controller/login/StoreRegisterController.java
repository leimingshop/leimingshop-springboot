/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.login;


import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.article.DocumentDTO;
import com.leimingtech.enums.document.DocumentEnum;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.dto.member.AuthorizationDTO;
import com.leimingtech.modules.dto.store.RegisterStoreDTO;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.member.CaptchaCodeService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.modules.utils.MobileUtil;
import com.leimingtech.seller.constant.SellerConstants;
import com.leimingtech.seller.service.CaptchaService;
import com.leimingtech.service.article.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author 程前
 * @version 1.0
 * @date 2019/12-18
 */
@Slf4j
@RestController
@RequestMapping("register")
@Api(tags = "店铺入驻")
public class StoreRegisterController {

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private CaptchaCodeService captchaCodeService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private StoreService storeService;

    @Autowired
    private DocumentService documentService;


    /**
     * 获取手机验证码
     *
     * @param params 手机号
     * @return
     */
    @GetMapping(value = "mobile/code")
    @ApiOperation(value = "获取手机验证码")
    @LogOperation(value = "获取手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "uuid", value = "uuid", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "captcha", value = "图形验证码", paramType = "query", required = true, dataType = "string")
    })
    public Result checkValidCode(@ApiIgnore @RequestParam Map<String, Object> params) {

        String mobile = params.get("mobile").toString();
        String uuid = params.get("uuid").toString();
        String captcha = params.get("captcha").toString();
        //手机号校验
        if (!MobileUtil.isMobile(mobile)) {
            return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
        }
        // 验证码校验
        boolean flag = captchaService.validate(SellerConstants.APPLICATION_NAME + "-" + uuid, captcha);
        if (!flag) {
            return new Result<AuthorizationDTO>().error(ErrorCode.CAPTCHA_ERROR);
        }
        // 手机号是否注册
        Integer count = storeUserService.verifyMobile(mobile, null);
        if (count > 0) {
            return new Result().error("该手机号已经注册");
        }
        captchaCodeService.moblieCaptcha(mobile, MemberRedisConstants.STORE_REGISTER_MOBILE_CODE_PREFIX);
        return new Result().ok(null, "发送成功");
    }

    /**
     * 校验手机号验证码是否一致
     *
     * @param params
     * @return
     */
    @GetMapping("verify/mobile/code")
    @ApiOperation("校验手机号验证码是否一致")
    @LogOperation(value = "校验手机号验证码是否一致")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "query", required = true, dataType = "string")
    })
    public Result verifyMobileCode(@ApiIgnore @RequestParam Map<String, Object> params) {
        String mobile = (String) params.get("mobile");
        String code = (String) params.get("code");
        //手机号校验
        if (!MobileUtil.isMobile(mobile)) {
            return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
        }
        String redisCode = (String) redisUtils.get(MemberRedisConstants.STORE_REGISTER_MOBILE_CODE_PREFIX + mobile);
        if (StringUtils.isBlank(redisCode)) {
            return new Result().error(MemberErrorEnum.E_CAPTCHA_PASS.code(), MemberErrorEnum.E_CAPTCHA_PASS.value());
        }
        // 校验验证码
        if (!StringUtils.equals(redisCode, code)) {
            return new Result().error(MemberErrorEnum.E_CAPTCHA_ERROR.code(), MemberErrorEnum.E_CAPTCHA_ERROR.value());
        }
        return new Result().ok(null, "校验通过");
    }


    /**
     * 校验手机号是否注册
     *
     * @param mobile
     * @return
     */
    @GetMapping("verify/mobile")
    @ApiOperation("校验手机号是否注册")
    @LogOperation(value = "校验手机号是否注册")
    public Result verify(@RequestParam String mobile) {
        if (!MobileUtil.isMobile(mobile)) {
            return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
        }
        Integer count = storeUserService.verifyMobile(mobile, null);
        if (count > 0) {
            return new Result().error("该手机号已注册，请直接登录");
        }

        return new Result().ok(null, "当前手机号可以使用");
    }

    /**
     * 校验用户名是否重复
     *
     * @param userName
     * @return
     */
    @GetMapping("verify/username")
    @ApiOperation("校验用户名是否重复")
    @LogOperation(value = "校验用户名是否重复")
    public Result verifyUsername(@RequestParam String userName) {

        if (!userName.matches(RegexConstant.SAVE_UPDATE_ACCOUNT_REGEX)) {
            return new Result().error("账号必须为6-20位字符，数字，下划线");
        }
        Integer verify = storeUserService.verify(userName, null, null);
        if (verify > 0) {
            return new Result().error("该用户名称已存在，请重新输入");
        }

        return new Result().ok(null, "该用户名可以使用");
    }

    @PostMapping
    @ApiOperation("注册账号")
    @LogOperation(value = "注册账号")
    public Result register(@RequestBody RegisterStoreDTO registerStoreDTO) {

        //rsa解密
        try {
            registerStoreDTO.setPassword(RSACoder.decryptByPrivateKey(registerStoreDTO.getPassword()));
            registerStoreDTO.setAccount(RSACoder.decryptByPrivateKey(registerStoreDTO.getAccount()));
            registerStoreDTO.setConfirmPassword(RSACoder.decryptByPrivateKey(registerStoreDTO.getConfirmPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", registerStoreDTO.getAccount(), e);
            return new Result().error("密码解析失败，请重新输入");
        }

        Integer verify = storeUserService.verify(registerStoreDTO.getAccount(), null, null);
        if (verify > 0) {
            return new Result().error("该用户名称已存在，请重新输入");
        }
        // 校验邮箱
        Integer count = storeUserService.verifyEmail(registerStoreDTO.getEmail(), null);
        if (count > 0) {
            return new Result().error("邮箱已经注册");
        }
        // 验证新密码
        if (!registerStoreDTO.getConfirmPassword().equals(registerStoreDTO.getPassword())) {
            return new Result().error("两次输入密码不一致");
        }

        if (!registerStoreDTO.getAccount().matches(RegexConstant.SAVE_UPDATE_ACCOUNT_REGEX)) {
            return new Result().error("账号必须为6-20位字符，数字，下划线");
        }
        if (!registerStoreDTO.getPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }
        //保存用户
        storeUserService.storeRegister(registerStoreDTO);

        return new Result().ok(null, "注册成功");
    }

    /**
     * 校验店铺名称是否重复
     *
     * @param storeName 店铺名称
     * @return
     */
    @GetMapping("verify/store/name")
    @ApiOperation("校验店铺名称是否重复")
    @LogOperation(value = "校验店铺名称是否重复")
    public Result register(@RequestParam("storeName") String storeName, @RequestParam(value = "storeId", required = false) Long storeId) {
        Integer count = storeService.findStoreName(storeName, storeId);
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }
        return new Result().ok(null, "可以使用");
    }

    /**
     * 获取用户协议或者入住须知
     *
     * @param type 1 入住须知，2 用户协议
     * @return
     */
    @GetMapping("notice/protocol")
    @ApiOperation("获取用户协议或者入住须知")
    @LogOperation(value = "获取用户协议或者入住须知")
    public Result<DocumentDTO> noticeProtocol(@RequestParam("type") Integer type) {
        DocumentDTO detailByCode;
        if (type == 1) {
            detailByCode = documentService.getDetailByCode(DocumentEnum.STORE_REGISTER.value());
        } else {
            detailByCode = documentService.getDetailByCode(DocumentEnum.REGISTER_PROTOCOL.value());
        }

        return new Result<DocumentDTO>().ok(detailByCode);
    }
}
