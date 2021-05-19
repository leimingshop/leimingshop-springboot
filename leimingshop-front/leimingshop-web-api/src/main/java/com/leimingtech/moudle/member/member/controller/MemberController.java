/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.leimingtech.captcha.CaptchaUtils;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DataMaskingUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.message.enums.EmailEnum;
import com.leimingtech.message.service.SysMailTemplateService;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.dto.member.*;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.member.CaptchaCodeService;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.utils.MobileUtil;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import com.leimingtech.moudle.member.member.code.MemberCode;
import com.leimingtech.moudle.member.member.vo.MemberBaseInfoVo;
import com.leimingtech.moudle.member.member.vo.MemberCenterPcVo;
import com.leimingtech.moudle.member.member.vo.MemberHeaderInfoVo;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("member")
@Api(tags = "会员")
public class MemberController {

    private static final String CODE_TYPE_VERIFY = "0";
    private static final String CODE_TYPE_UPDTAE = "1";
    private static final String MAP_PARAMS_CODE_STR = "code";
    private static final String MAP_PARAMS_CODE_TYPE_STR = "codeType";
    private static final String MAP_PARAMS_OLD_PWD_STR = "oldPwd";


    @Autowired
    private CaptchaCodeService captchaService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private SysMailTemplateService sysMailTemplateService;

    @Autowired
    private CaptchaUtils captchaUtils;

    @Value("${system.address.prefix}")
    private String systemUrl;

    @GetMapping("member/home")
    @ApiOperation("个人中心")
    @LogContext(code = MemberCode.MEMBER_CENTER_CODE, note = MemberCode.MEMBER_CENTER_DESC)
    public Result<MemberCenterPcVo> personDetail() {
        // 获取memberId
        Long memberId = null;
        try {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<MemberCenterPcVo>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        MemberPersonCenterPcDTO personCenterPcDTO = memberService.selectPcMemberDetail(memberId);
        MemberCenterPcVo memberCenterPcVo = ConvertUtils.sourceToTarget(personCenterPcDTO, MemberCenterPcVo.class);
        return new Result<MemberCenterPcVo>().ok(memberCenterPcVo);
    }

    @GetMapping("header/info")
    @ApiOperation("用户公共头部数据")
    @LogContext(code = MemberCode.MEMBER_CENTER_CODE, note = MemberCode.MEMBER_CENTER_DESC)
    public Result<MemberHeaderInfoVo> getMemberHeaderInfo() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        MemberDTO userDetail = SecurityCurrentUser.getUserDetail();
        MemberHeaderInfoVo memberHeaderInfoVo = ConvertUtils.sourceToTarget(userDetail, MemberHeaderInfoVo.class);
        return new Result<MemberHeaderInfoVo>().ok(memberHeaderInfoVo);
    }


    @GetMapping("base/info")
    @ApiOperation("用户个人信息获取")
    @LogContext(code = MemberCode.MEMBER_CENTER_CODE, note = MemberCode.MEMBER_CENTER_DESC)
    public Result<MemberBaseInfoVo> getMemberInfo() {
        // 获取memberId
        Long memberId = null;
        try {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<MemberBaseInfoVo>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        MemberBaseInfoDTO memberBaseInfoDTO = memberService.selectPcMemberBaseInfo(memberId);
        MemberBaseInfoVo memberBaseInfoVo = ConvertUtils.sourceToTarget(memberBaseInfoDTO, MemberBaseInfoVo.class);
        return new Result<MemberBaseInfoVo>().ok(memberBaseInfoVo);
    }


    @PutMapping(value = "base/info")
    @ApiOperation(value = "个人信息修改")
    @LogContext(code = MemberCode.MEMBER_INFO_UPDATE_CODE, note = MemberCode.MEMBER_INFO_UPDATE_DESC)
    public Result memberInfoUpdate(@RequestBody MemberBaseInfoVo memberBaseInfoVo) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        MemberUpdateDTO memberUpdateDTO = ConvertUtils.sourceToTarget(memberBaseInfoVo, MemberUpdateDTO.class);
        memberUpdateDTO.setId(SecurityCurrentUser.getUserDetail().getId());
        memberUpdateDTO.setMemberName(SecurityCurrentUser.getUserDetail().getMemberName());
        memberService.updateMemberBaseInfo(memberUpdateDTO);

        return new Result().ok(null, "个人信息修改成功");
    }


    @GetMapping(value = "mobilecode")
    @ApiOperation(value = "获取短信验证码(更换手机号使用)")
    @LogContext(code = MemberCode.MEMBER_CHANGE_PHONE_CODE_CODE, note = MemberCode.MEMBER_CHANGE_PHONE_CODE_DESC)
    public Result getMobileCode(@ApiParam(name = "mobile", value = "手机号", required = false) @RequestParam(value = "mobile", required = false) String mobile,
                                @ApiParam(name = "codeType", value = "0：验证身份;1：修改手机号", required = true) @RequestParam(value = "codeType") Integer codeType) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        if (codeType.equals(Integer.valueOf(CODE_TYPE_VERIFY))) {
            mobile = SecurityCurrentUser.getUserDetail().getMemberMobile();
            // 发送短信
            captchaService.moblieCaptcha(mobile, MemberRedisConstants.MOBILE_BIND_CODE_PREFIX + CODE_TYPE_VERIFY + ":" + SecurityCurrentUser.getUserDetail().getId() + ":");
        } else {
            if (StringUtils.isBlank(mobile) || !MobileUtil.isMobile(mobile)) {
                return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
            }
            // 发送短信
            captchaService.moblieCaptcha(mobile, MemberRedisConstants.MOBILE_BIND_CODE_PREFIX + CODE_TYPE_UPDTAE + ":" + SecurityCurrentUser.getUserDetail().getId() + ":");
        }
        return new Result().ok(null, "发送成功");
    }

    @PutMapping(value = "verify/code")
    @ApiOperation(value = "修改手机号验证码校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "code", value = "短信验证码", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "codeType", value = "0：验证身份;1：修改手机号", paramType = "query", required = true, dataType = "String")
    })
    @LogContext(code = MemberCode.MEMBER_VERIFY_CHANGE_PHONE_CODE, note = MemberCode.MEMBER_VERIFY_CHANGE_PHONE_DESC)
    public Result memberPhoneUpdate(@ApiIgnore @RequestParam Map<String, String> params) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        String codeType = params.get("codeType");
        String code = params.get("code");
        String mobile = params.get("mobile");
        MemberDTO userDetail = SecurityCurrentUser.getUserDetail();
        if (StringUtils.isBlank(code)) {
            return new Result().error(MemberErrorEnum.E_WECHAT_CODE_ERROR.code(), MemberErrorEnum.E_WECHAT_CODE_ERROR.value());
        }
        if (StringUtils.isBlank(codeType)) {
            return new Result().error(MemberErrorEnum.E_CAPTCHA_TYPE_ERROR.code(), MemberErrorEnum.E_CAPTCHA_TYPE_ERROR.value());
        }
        if (CODE_TYPE_UPDTAE.equals(codeType)) {
            if (StringUtils.isBlank(mobile) || !MobileUtil.isMobile(mobile)) {
                return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
            }
        } else {
            mobile = userDetail.getMemberMobile();
        }

        String redisKey = MemberRedisConstants.MOBILE_BIND_CODE_PREFIX + codeType + ":" + userDetail.getId() + ":" + mobile;
        Object o = redisUtils.get(redisKey);
        if (BeanUtil.isEmpty(o)) {
            return new Result().error(MemberErrorEnum.E_CAPTCHA_PASS.code(), MemberErrorEnum.E_CAPTCHA_PASS.value());
        }
        if (params.get(MAP_PARAMS_CODE_STR).equals(String.valueOf(o))) {
            //查询手机号是否已注册
            if (CODE_TYPE_UPDTAE.equals(params.get(MAP_PARAMS_CODE_TYPE_STR))) {
                MemberPhoneDTO memberPhoneDTO = memberService.selectByMobile(mobile);
                if (!BeanUtil.isEmpty(memberPhoneDTO)) {
                    return new Result().error(MemberErrorEnum.E_MOBILE_USE.code(), MemberErrorEnum.E_MOBILE_USE.value());
                }
                userDetail.setMemberMobile(mobile);
                redisUtils.set(RedisKeys.getSecurityUserKey(userDetail.getMemberName()), userDetail, RedisUtils.NOT_EXPIRE);
                MemberDTO memberDTO = new MemberDTO();
                memberDTO.setId(SecurityCurrentUser.getUserDetail().getId());
                memberDTO.setMemberMobile(mobile);
                memberService.updateById(memberDTO);
                return new Result().ok(null, "手机号更换成功");
            }
            return new Result().ok(null, MemberErrorEnum.E_REGISTER_SUCCESS.value());
        }
        return new Result().error(MemberErrorEnum.E_CAPTCHA_ERROR.code(), MemberErrorEnum.E_CAPTCHA_ERROR.value());
    }

    /**
     * 获取手机验证码
     *
     * @param params 手机号
     * @return
     */
    @GetMapping(value = "mobile/code")
    @ApiOperation(value = "获取会员登陆手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "uuid", value = "uuid", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "captcha", value = "图形验证码", paramType = "query", required = true, dataType = "string")
    })
    @LogContext(code = MemberCode.MEMBER_LOGIN_CODE, note = MemberCode.MEMBER_LOGIN_DESC)
    public Result checkValidCode(@ApiIgnore @RequestParam Map<String, Object> params) {
        String mobile = params.get("mobile").toString();
        String uuid = params.get("uuid").toString();
        String captcha = params.get("captcha").toString();
        //手机号校验
        if (!MobileUtil.isMobile(mobile)) {
            return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
        }
        // 校验图形验证码是否正确
        if (captchaUtils.validate(uuid, captcha)) {
            captchaService.moblieCaptcha(mobile, MemberRedisConstants.LOGIN_MOBILE_CODE_PREFIX);
            return new Result().ok(null, "发送成功");
        } else {
            return new Result().error(null, "图形验证码错误");
        }

    }

    /**
     * 修改密码,根据用户名判断密码
     *
     * @param params
     * @return
     */
    @PutMapping(value = "change/password")
    @ApiOperation(value = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPwd", value = "原密码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "newPwd", value = "新密码", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "ConfirmPwd", value = "确认密码", paramType = "query", required = true, dataType = "string")
    })
    @LogContext(code = MemberCode.MEMBER_CHANGE_PWD_BY_OLD_CODE, note = MemberCode.MEMBER_CHANGE_PWD_BY_OLD_DESC)
    public Result updatePasswdByPasswd(@ApiIgnore @RequestParam Map<String, Object> params) {

        MemberDTO userDetail = null;
        MemberDTO memberDTO = null;
        try {
            // 获取用户信息
            userDetail = SecurityCurrentUser.getUserDetail();
            memberDTO = memberService.getMemberPassword(userDetail.getId());
            if (memberDTO == null) {
                return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "查询旧密码失败");
            }
        } catch (CustomException e) {
            return new Result().error(401, "请先登录");
        }
        //验证密码是否正确
        String oldPwd = "";
        String newPwd = params.get("newPwd").toString();
        String confirmPwd = params.get("ConfirmPwd").toString();

        // 密码解密
        // 使用RSA私钥进行解密
        try {
            if (params.get(MAP_PARAMS_OLD_PWD_STR) != null) {
                if (params.get(MAP_PARAMS_OLD_PWD_STR) != null) {
                    oldPwd = params.get(MAP_PARAMS_OLD_PWD_STR).toString();
                }
                if (StringUtils.isNotBlank(oldPwd)) {
                    oldPwd = oldPwd.replace(" ", "+");
                    oldPwd = (RSACoder.decryptByPrivateKey(oldPwd));
                }
            }

            newPwd = newPwd.replace(" ", "+");
            newPwd = (RSACoder.decryptByPrivateKey(newPwd));

            confirmPwd = confirmPwd.replace(" ", "+");
            confirmPwd = (RSACoder.decryptByPrivateKey(confirmPwd));

        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", e);
            throw new ServiceException("密码解析失败，请重新输入");
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(memberDTO.getMemberPasswd())) {
            // 原密码不为空校验原密码是否正确
            if (org.apache.commons.lang3.StringUtils.isBlank(oldPwd)) {
                return new Result().error(ErrorCode.FORBIDDEN, "旧密码不能为空");
            }
            if (!PasswordUtils.matches(oldPwd, memberDTO.getMemberPasswd())) {
                return new Result().error(10010, "原密码错误");
            }
        }
        //新密码对比
        if (!newPwd.equals(confirmPwd)) {
            return new Result().error(10011, "新密码与确认密码不一致");
        }
        if (!newPwd.matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }
        //新密码加密
        String encodeNewPasswd = PasswordUtils.encode(newPwd);
        //修改密码
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        memberUpdateDTO.setId(userDetail.getId());
        memberUpdateDTO.setMemberPasswd(encodeNewPasswd);
        memberService.updateMember(memberUpdateDTO);
        return new Result().ok(null, "修改成功");
    }


    /**
     * 获取用户邮箱
     */
    @GetMapping(value = "mail/info")
    @ApiOperation(value = "获取邮箱地址")
    @LogContext(code = MemberCode.MEMBER_GET_MAIL_CODE_CODE, note = MemberCode.MEMBER_GET_MAIL_CODE_DESC)
    public Result getMailInfo() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        String memberEmail = SecurityCurrentUser.getUserDetail().getMemberEmail();
        memberEmail = (DataMaskingUtils.maskEmail(memberEmail));

        return new Result().ok(memberEmail, "获取成功");
    }

    /**
     * 换绑邮箱发送验证码
     */
    @GetMapping(value = "changemail")
    @ApiOperation(value = "换绑邮箱发送验证码")
    @LogContext(code = MemberCode.MEMBER_CHANGE_EMAIL_CODE_CODE, note = MemberCode.MEMBER_CHANGE_EMAIL_CODE_DESC)
    public Result changeMail(
            @ApiParam(name = "email", value = "邮箱", required = false) @RequestParam(value = "email", required = false) String email,
            @ApiParam(name = "codeType", value = "0：验证身份;1：修改邮箱", required = true) @RequestParam(value = "codeType") String codeType) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录后操作");
        }
        MemberDTO userDetail = SecurityCurrentUser.getUserDetail();
        if (CODE_TYPE_VERIFY.equals(codeType)) {
            email = SecurityCurrentUser.getUserDetail().getMemberEmail();
            if (StringUtils.isBlank(email)) {
                return new Result().error("您还未绑定邮箱，请先绑定邮箱");
            }
        }
        memberService.changeEmailSendCaptcha(email, SecurityCurrentUser.getUserDetail().getId(), codeType);
        return new Result().ok(null, "验证码已发送，请去邮箱查看");
    }


    @PutMapping(value = "/changemail")
    @ApiOperation(value = "更改邮箱验证码校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "code", value = "邮箱验证码", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "codeType", value = "0：验证身份;1：修改邮箱", paramType = "query", required = true, dataType = "String")
    })
    @LogContext(code = MemberCode.MEMBER_VERIFY_CHANGE_EMAIL_CODE, note = MemberCode.MEMBER_VERIFY_CHANGE_EMAIL_DESC)
    public Result<MemberVoDTO> verifyMailCode(@ApiIgnore @RequestParam Map<String, String> params) {
        AssertUtils.isMapEmpty(params, "参数不能为空");

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<MemberVoDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        String email = params.get("email");
        String code = params.get("code");
        String codeType = params.get("codeType");
        if (StringUtils.isBlank(email)) {
            return new Result<MemberVoDTO>().error(MemberErrorEnum.E_EMAIL_ERROR.code(), MemberErrorEnum.E_EMAIL_ERROR.value());
        }
        if (StringUtils.isBlank(code)) {
            return new Result<MemberVoDTO>().error(MemberErrorEnum.E_CAPTCHA_ERROR.code(), MemberErrorEnum.E_CAPTCHA_ERROR.value());
        }
        if (StringUtils.isBlank(codeType)) {
            return new Result<MemberVoDTO>().error(MemberErrorEnum.E_CAPTCHA_TYPE_ERROR.code(), MemberErrorEnum.E_CAPTCHA_TYPE_ERROR.value());
        }
        if (CODE_TYPE_VERIFY.equals(codeType)) {
            params.put("email", SecurityCurrentUser.getUserDetail().getMemberEmail());
        }
        params.put("memberId", String.valueOf(SecurityCurrentUser.getUserDetail().getId()));
        params.put("memberName", String.valueOf(SecurityCurrentUser.getUserDetail().getMemberName()));
        memberService.changeEmail(params);
        if (CODE_TYPE_VERIFY.equals(codeType)) {
            return new Result<MemberVoDTO>().ok(null, MemberErrorEnum.E_REGISTER_SUCCESS.value());
        } else {
            return new Result<MemberVoDTO>().ok(null, "邮箱换绑成功");
        }

    }

    /**
     * 绑定邮箱
     */
    @GetMapping(value = "send/mailcode/{email}")
    @ApiOperation(value = "绑定邮箱")
    @LogContext(code = MemberCode.MEMBER_BIND_EMAIL_SEND_CODE, note = MemberCode.MEMBER_BIND_EMAIL_SEND_DESC)
    public Result sendMailCode(@PathVariable("email") String email) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录后操作");
        }
        Map<String, String> params = new HashMap<>(5);
        String memberName = SecurityCurrentUser.getUserName();
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        //生成验证码，在回调方法中验证
        String code = IdUtil.randomUUID();
        String path = "?code=" + code + "%26memberId=" + memberId;
        //${email}、${memberName}、${path}
        params.put("email", email);
        params.put("memberName", memberName);
        params.put("memberId", String.valueOf(memberId));
        //保存数据在缓存，回调中获取校验
        redisUtils.set(MemberRedisConstants.MEMBER_BIND_EMAIL + code, params, MemberRedisConstants.EMAIL_CACHE_TIME);
        params.put("path", path);
        //1262984476460322818
        sysMailTemplateService.sendMail(Long.valueOf(EmailEnum.ACCOUNT_BIND_EMAIL_TEMPLATE.value()), email, "", JSON.toJSONString(params));
        return new Result().ok(null, "邮件已发送，请去邮箱确认");
    }

    /**
     * 邮箱确认回调
     *
     * @author xuzhch
     * @date 2020年5月20日15:26:30
     */
    @GetMapping(value = "bind/email")
    @LogContext(code = MemberCode.MEMBER_BIND_EMAIL_CALLBACK_CODE, note = MemberCode.MEMBER_BIND_EMAIL_CALLBACK_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "用户Id", paramType = "query", required = true, dataType = "String"),
    })
    public Result bindMemberEmail(@ApiIgnore @RequestParam Map<String, String> params) {
        String code = params.get("code");
        String memberIdStr = params.get("memberId");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(memberIdStr)) {
            return new Result().error("绑定失败，请重试");
        }
        memberService.bindEmail(params);
        return new Result().ok(null, "绑定成功");
    }
}
