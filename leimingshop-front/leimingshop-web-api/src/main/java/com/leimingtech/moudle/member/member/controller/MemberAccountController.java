/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.controller;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.member.CaptchaCodeService;
import com.leimingtech.modules.utils.MobileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员账号管理
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("member/account")
@Api(tags = "会员账号管理")
public class MemberAccountController {
    @Autowired
    private CaptchaCodeService captchaService;

    @GetMapping(value = "mobile/bind/{mobile}")
    @ApiOperation(value = "获取第三方登陆手机绑定短信验证码")
    public Result moblieBindCaptcha(@PathVariable(value = "mobile") String mobile) {
        if (MobileUtil.isMobile(mobile)) {
            // 发送短信
            captchaService.moblieCaptcha(mobile, MemberRedisConstants.MOBILE_BIND_CODE_PREFIX);
            return new Result().ok(null, "发送成功");
        } else {
            return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
        }
    }
}
