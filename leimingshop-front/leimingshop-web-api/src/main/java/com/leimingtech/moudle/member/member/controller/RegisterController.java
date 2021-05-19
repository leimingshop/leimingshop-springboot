/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.controller;

import com.leimingtech.captcha.CaptchaUtils;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.constant.PcConstants;
import com.leimingtech.dto.article.DocumentDTO;
import com.leimingtech.enums.document.DocumentEnum;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.dto.member.AuthorizationDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.member.MemberPhoneDTO;
import com.leimingtech.modules.enums.member.LoginEnum;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.member.CaptchaCodeService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.utils.MobileUtil;
import com.leimingtech.moudle.member.member.code.MemberCode;
import com.leimingtech.service.article.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName RegisterController
 * @Description
 * @Author DY
 * @Date 2019/5/16 10:12
 * @Version 1.0
 **/
@RestController
@RequestMapping("member/register")
@Api(tags = "注册")
public class RegisterController {

    private static final String MAP_PARAMS_EMAIL_STR = "memberEmail";
    /**
     * 协议类型 1 商家入住协议
     */
    private static final Integer STORE_AGREEMENT = 1;
    /**
     * 协议类型 2 注册协议
     */
    private static final Integer REGIST_AGREEMENT = 2;
    /**
     * 协议类型  3 用户协议
     */
    private static final Integer MEMBER_AGREEMENT = 3;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CaptchaCodeService captchaService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CaptchaUtils captchaUtils;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 数据验证 邮箱,手机号
     */
    @PostMapping("verify")
    @ApiOperation("注册-是否已注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberEmail", value = "邮箱", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "memberName", value = "用户名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "memberMobile", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "loginType", value = "注册类型(0:用户名,1:邮箱,2手机号;)", paramType = "query", required = true, dataType = "int")
    })
    @LogContext(code = MemberCode.MEMBER_VERIFY_REGISTER_CODE, note = MemberCode.MEMBER_VERIFY_REGISTER_DESC)
    public Result<MemberPhoneDTO> verify(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 校验参数是否正确
        Integer loginType = Integer.valueOf(params.get("loginType").toString());
        String memberMobile = (String) params.get("memberMobile");
        String memberEmail = (String) params.get("memberEmail");
        String memberName = (String) params.get("memberName");

        if (LoginEnum.LOGIN_MOBILE.value() == loginType) {
            //手机注册
            if (StringUtils.isBlank(memberMobile) || !MobileUtil.isMobile(memberMobile)) {
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
            }

            // 查询手机号是否存在
            MemberPhoneDTO memberDTO = memberService.selectByMobile(memberMobile);
            if (memberDTO != null) {
                //手机号已注册---根据手机号查询用户信息
                MemberPhoneDTO memberPhoneDTO = new MemberPhoneDTO();
                memberPhoneDTO.setNickName(memberDTO.getNickName());
                memberPhoneDTO.setMemberAvatar(memberDTO.getMemberAvatar());
                memberPhoneDTO.setMemberName(memberDTO.getMemberName());
                return new Result<MemberPhoneDTO>().ok(memberPhoneDTO, MemberErrorEnum.E_REGISTER_SUCCESS.value());
            } else {
                //未使用手机号注册
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_MOBILE_IS_NULL.code(), MemberErrorEnum.E_MOBILE_IS_NULL.value());
            }

        } else if (LoginEnum.LOGIN_EMAIL.value() == loginType) {
            //邮箱注册
            if (StringUtils.isBlank(memberEmail)) {
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_EMAIL_ERROR.code(), MemberErrorEnum.E_EMAIL_ERROR.value());
            } else if (memberService.selectMemberByMemberEmail(params.get(MAP_PARAMS_EMAIL_STR).toString())) {
                //已被注册
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_EMAIL_REGISTERED.code(), MemberErrorEnum.E_EMAIL_REGISTERED.value());
            }
        } else if (LoginEnum.LOGIN_MEMBERNAME.value() == loginType) {
            //用户名注册
            if (StringUtils.isBlank(memberName)) {
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_MEMBERNAME_ERROR.code(), MemberErrorEnum.E_MEMBERNAME_ERROR.value());
            }

            MemberDTO memberDTO = memberService.selectMemberByUserName(memberName);

            if (null == memberDTO) {
                return new Result<MemberPhoneDTO>().ok(null, MemberErrorEnum.E_REGISTER_SUCCESS.value());
            }

            if (null != memberDTO || null != memberDTO.getId()) {
                //已被注册
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_MEMBERNAME_REGISTERED.code(), MemberErrorEnum.E_MEMBERNAME_REGISTERED.value());
            }

        } else {
            return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_LOGIN_TYPE.code(), MemberErrorEnum.E_LOGIN_TYPE.value());
        }

        return new Result<MemberPhoneDTO>().ok(null, MemberErrorEnum.E_REGISTER_SUCCESS.value());
    }

    /**
     * 注册
     */
    @PostMapping
    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberEmail", value = "邮箱", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "memberName", value = "用户名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "memberMobile", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "checkValidCode", value = "验证码", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "memberSource", value = "设备标识（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberPasswd", value = "密码", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "loginType", value = "注册类型:1邮箱注册;2手机注册", paramType = "query", required = true, dataType = "int")
    })
    @LogContext(code = MemberCode.MEMBER_REGISTER_CODE, note = MemberCode.MEMBER_REGISTER_DESC)
    public Result register(@ApiIgnore @RequestParam Map<String, Object> params) {

        // 校验参数是否正确
        Integer loginType = Integer.valueOf(params.get("loginType").toString());
        String memberMobile = (String) params.get("memberMobile");
        String memberEmail = (String) params.get("memberEmail");
        String memberName = (String) params.get("memberName");

        if (LoginEnum.LOGIN_MOBILE.value() == loginType) {
            //手机注册
            if (StringUtils.isBlank(memberMobile) || !MobileUtil.isMobile(memberMobile)) {
                return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
            }

            //用户名注册
            if (StringUtils.isBlank(memberName)) {
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_MEMBERNAME_ERROR.code(), MemberErrorEnum.E_MEMBERNAME_ERROR.value());
            }

            MemberDTO memberDTO = memberService.selectMemberByUserName(memberName);

            if (null == memberDTO) {

            } else {
                //已被注册
                return new Result<MemberPhoneDTO>().error(MemberErrorEnum.E_MEMBERNAME_REGISTERED.code(), MemberErrorEnum.E_MEMBERNAME_REGISTERED.value());
            }


            // 查询手机号是否存在
            Boolean result = memberService.selectMemberByUsermaneOrMobile(memberMobile);
            if (result) {
                //手机号已注册---根据手机号查询用户信息
                return new Result().error(MemberErrorEnum.E_MOBILE_IS_EXIT.code(), MemberErrorEnum.E_MOBILE_IS_EXIT.value());
            } else {
                //未使用手机号注册 进行手机号注册
                Map<String, Object> map = memberService.saveMemberLongin(params);
                Integer code = (Integer) map.get("code");
                String msg = (String) map.get("msg");
                if (code == ErrorCode.SUCCESS) {
                    return new Result().ok(null, "注册成功");
                } else {
                    return new Result().error(code, msg);
                }
            }

        } else if (LoginEnum.LOGIN_EMAIL.value() == loginType) {
            //邮箱注册
            if (StringUtils.isBlank(memberEmail)) {
                return new Result().error(MemberErrorEnum.E_EMAIL_ERROR.code(), MemberErrorEnum.E_EMAIL_ERROR.value());
            } else if (memberService.selectMemberByMemberEmail(params.get(MAP_PARAMS_EMAIL_STR).toString())) {
                //已被注册
                return new Result().error(MemberErrorEnum.E_EMAIL_REGISTERED.code(), MemberErrorEnum.E_EMAIL_REGISTERED.value());
            }
        } else {
            return new Result().error(MemberErrorEnum.E_LOGIN_TYPE.code(), MemberErrorEnum.E_LOGIN_TYPE.value());
        }

        return new Result().error(MemberErrorEnum.E_REGISTER_FAILED.code(), MemberErrorEnum.E_REGISTER_FAILED.value());
    }

    /**
     * 获取用户注册手机验证码
     *
     * @param params 手机号
     * @return
     */
    @GetMapping(value = "mobile/code")
    @ApiOperation(value = "注册-获取会员注册手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "uuid", value = "uuid", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "captcha", value = "验证码", paramType = "query", required = true, dataType = "string")

    })
    @LogContext(code = MemberCode.MEMBER_REGISTER_PHONE_CODE, note = MemberCode.MEMBER_REGISTER_PHONE_DESC)
    public Result checkValidCode(@ApiIgnore @RequestParam Map<String, Object> params) {
        String mobile = params.get("mobile").toString();
        String uuid = params.get("uuid").toString();
        String captcha = params.get("captcha").toString();

        //手机号校验
        if (!MobileUtil.isMobile(mobile)) {
            return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
        }

        // 验证码校验
        boolean flag = captchaUtils.validate(PcConstants.APPLICATION_NAME + "-" + uuid, captcha);
        if (!flag) {
            return new Result<>().error(ErrorCode.CAPTCHA_ERROR);
        }

        // 校验手机号是否存在
        Boolean result = memberService.selectMemberByUsermaneOrMobile(mobile);
        if (result) {
            return new Result().error(MemberErrorEnum.E_MOBILE_IS_EXIT.code(), MemberErrorEnum.E_MOBILE_IS_EXIT.value());
        }

        captchaService.moblieCaptcha(mobile, MemberRedisConstants.REGISTER_MOBILE_CODE_PREFIX);

        return new Result().ok(null, "发送成功");
    }

    /**
     * 获取用户协议或者入住须知
     *
     * @param type 1 入住须知，2 注册协议 3用户协议
     * @return
     */
    @GetMapping("notice/protocol")
    @ApiOperation("注册-获取用户协议或者入住须知(1 商家入住协议，2 注册协议 3用户协议)")
    @LogOperation(value = "获取用户协议或者入住须知")
    public Result<DocumentDTO> noticeProtocol(@RequestParam("type") Integer type) {
        DocumentDTO detailByCode;
        if (type.equals(STORE_AGREEMENT)) {
            detailByCode = documentService.getDetailByCode(DocumentEnum.STORE_REGISTER.value());
        } else if (type.equals(MEMBER_AGREEMENT)) {
            detailByCode = documentService.getDetailByCode(DocumentEnum.USER_PROTOCOL.value());
        } else {
            detailByCode = documentService.getDetailByCode(DocumentEnum.REGISTER_PROTOCOL.value());
        }

        return new Result<DocumentDTO>().ok(detailByCode);
    }

    @GetMapping("generate/image")
    @ApiOperation(value = "注册-获取图片验证码", produces = "application/octet-stream")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "uuid", required = true)
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        //uuid不能为空
        AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);
        //生成图片验证码
        captchaUtils.create(response, PcConstants.APPLICATION_NAME + "-" + uuid);
    }

    /**
     * 校验图形验证码
     *
     * @return
     */
    @GetMapping("verify/captcha")
    @ApiOperation(value = "注册-校验图形验证码")
    public Result forGetPwd(@RequestParam("uuid") String uuid, @RequestParam("captcha") String captcha) {
        // 验证码校验
        boolean flag = captchaUtils.validate(PcConstants.APPLICATION_NAME + "-" + uuid, captcha);
        if (!flag) {
            return new Result<AuthorizationDTO>().error(ErrorCode.CAPTCHA_ERROR);
        }
        return new Result().ok(null, "校验通过");
    }

    /**
     * 获取修改密码手机验证码
     *
     * @param params 手机号
     * @return
     */
    @GetMapping(value = "update/password/mobile/code")
    @ApiOperation(value = "忘记密码-获取修改密码手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "string")
    })
    public Result checkUpdatePasswordMobileValidCode(@ApiIgnore @RequestParam Map<String, Object> params) {
        String mobile = params.get("mobile").toString();

        //手机号校验
        if (!MobileUtil.isMobile(mobile)) {
            return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
        }

        //查询手机号是否已注册
        MemberPhoneDTO memberPhoneDTO = memberService.selectByMobile(mobile);
        if (memberPhoneDTO == null) {
            return new Result().error("手机号未注册");
        }
        // 获取手机验证码
        captchaService.moblieCaptcha(mobile, MemberRedisConstants.UPDATE_PWD_MOBILE_CODE_PREFIX);

        return new Result().ok(null, "发送成功");

    }

    /**
     * @Author: weixianchun
     * @Description: 验证码校验
     * @Date :2019/7/25 9:15
     * @Param code 验证码
     * @Param mobile 手机号
     * @Version V1.0
     **/
    @PostMapping(value = "compare/code")
    @ApiOperation(value = "忘记密码-验证码校验")
    public Result compareCode(@RequestParam("code") String code, @RequestParam("mobile") String mobile) {
        //从redis中获取验证码比较
        Object object = redisUtils.get(MemberRedisConstants.UPDATE_PWD_MOBILE_CODE_PREFIX + mobile);
        if (object == null) {
            return new Result().error(MemberErrorEnum.E_CAPTCHA_ERROR.code(), MemberErrorEnum.E_CAPTCHA_ERROR.value());
        }
        String redisCode = object.toString();
        if (StringUtils.isBlank(redisCode)) {
            return new Result().error(MemberErrorEnum.E_CAPTCHA_PASS.code(), MemberErrorEnum.E_CAPTCHA_PASS.value());
        }
        if (!code.equals(redisCode)) {
            return new Result().error(MemberErrorEnum.E_CAPTCHA_ERROR.code(), MemberErrorEnum.E_CAPTCHA_ERROR.value());
        } else {
            // 匹配通过，清除redis
            redisUtils.delete(MemberRedisConstants.UPDATE_PWD_MOBILE_CODE_PREFIX + mobile);
        }
        return new Result().ok(null, "验证码匹配成功");
    }

    /**
     * 忘记密码:根据手机号修改密码
     *
     * @return
     */
    @PutMapping(value = "forget/password")
    @ApiOperation(value = "忘记密码-根据手机号修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "newPwd", value = "新密码", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "ConfirmPwd", value = "确认密码", paramType = "query", required = true, dataType = "string")
    })
    @LogContext(code = MemberCode.MEMBER_FORGET_PASSWORD_CODE, note = MemberCode.MEMBER_FORGET_PASSWORD_DESC)
    public Result updatePasswordByMobile(@ApiIgnore @RequestParam Map<String, Object> params) {

        String mobile = params.get("mobile").toString();

        //手机号校验
        if (StringUtils.isNotBlank(mobile) && MobileUtil.isMobile(mobile)) {
            //查询手机号是否已注册
            MemberPhoneDTO memberPhoneDTO = memberService.selectByMobile(mobile);
            if (memberPhoneDTO == null) {
                return new Result().error("手机号未注册");
            }

            //修改密码
            memberService.updatePasswordByMobile(params);
            return new Result().ok(null, "修改成功");
        } else {
            return new Result().error("请填写正确手机号");
        }
    }
}
