/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils.member;

import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.dto.setting.SettingDefaultAvatarsDTO;
import com.leimingtech.modules.dto.log.MemberLoginLogDTO;
import com.leimingtech.modules.entity.member.MemberEntity;
import com.leimingtech.modules.utils.UserAgentUtils;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName MemberUtil
 * @Description 用户实体加工
 * @Author DY
 * @Date 2019/5/25 14:00
 * @Version 1.0
 **/
@Slf4j
@Component
public class MemberUtil {

    private static SettingService staticsettingService;
    @Autowired
    private SettingService settingService;

    private MemberUtil() {
    }

    /**
     * 用户登录修改用户登录ip和时间
     *
     * @param memberEntity
     * @return
     */
    public static MemberEntity updateMember(MemberEntity memberEntity) {
        //获取上次的当前ip/时间作为最后登录ip/时间
        memberEntity.setLastLoginIp(memberEntity.getMemberLoginIp());
        memberEntity.setLastLoginDate(memberEntity.getMemberLoginTime());
        //当前登录时间
        memberEntity.setMemberLoginTime(new Date());
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //当前登录ip
        memberEntity.setMemberLoginIp(IpUtils.getIpAddr(request));
        return memberEntity;
    }

    /**
     * 数据填充
     *
     * @param memberEntity
     * @return
     */
    public static MemberEntity saveMemberFill(MemberEntity memberEntity) {
        if (StringUtils.isEmpty(memberEntity.getMemberAvatar())) {
            //设置用户默认头像,昵称(给已注册用户返回时用)
            SettingDefaultAvatarsDTO defaultAvatarsSet = staticsettingService.getDefaultAvatarsSet();
            if (null == defaultAvatarsSet) {
                log.error("查询用户默认头像设置异常");
            }
            memberEntity.setMemberAvatar(String.valueOf(defaultAvatarsSet.getAvatar()));
        }
        //填充用户名
        if (StringUtils.isBlank(memberEntity.getMemberName())) {
            if (StringUtils.isNotBlank(memberEntity.getMemberEmail())) {
                //邮箱注册使用邮箱为用户名
                memberEntity.setMemberName(memberEntity.getMemberEmail());
            }
            if (StringUtils.isNotBlank(memberEntity.getMemberMobile())) {
                //手机注册使用手机为用户名
                memberEntity.setMemberName(memberEntity.getMemberMobile());
            }
        }
        return memberEntity;
    }

    /**
     * 保存登录日志工具类
     *
     * @param memberEntity
     * @return
     */
    public static MemberLoginLogDTO saveLoginLog(MemberEntity memberEntity) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        MemberLoginLogDTO memberLoginLogDTO = new MemberLoginLogDTO();
        memberLoginLogDTO.setIp(IpUtils.getIpAddr(request));
        memberLoginLogDTO.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        memberLoginLogDTO.setLoginName(memberEntity.getMemberName());
        memberLoginLogDTO.setMemberId(memberEntity.getId());
        if (UserAgentUtils.isComputer(request)) {
            memberLoginLogDTO.setSource(0);
        } else if (UserAgentUtils.isMobile(request)) {
            memberLoginLogDTO.setSource(1);
        } else {
            memberLoginLogDTO.setSource(2);
        }
        return memberLoginLogDTO;
    }

    @PostConstruct
    public void init() {
        staticsettingService = settingService;
    }
}
