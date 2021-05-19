/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.sms;

import com.leimingtech.commons.tools.utils.DataMaskingUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.front.entity.LmshopMember;
import com.leimingtech.modules.dto.member.MemberRegisterDTO;
import com.leimingtech.modules.dto.member.SecurityDTO;
import com.leimingtech.modules.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.leimingtech.modules.enums.member.MemberEnum.MEMBER_STATE_OFF;

/**
 * 短信验证码 UserDetailsService
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/11/27 22:01
 **/
@Service
@Slf4j
public class SmsCodeUserServiceDetailsImpl implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    @Value("${oauth.client}")
    private String client;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String memberSource = request.getHeader("memberSource");
        SecurityDTO securityDTO = memberService.selectSecurityUserInfo(s);

        LmshopMember lmshopMember = new LmshopMember();
        log.info("查询出来的用户实体为{}", lmshopMember);
        if (securityDTO != null) {
            lmshopMember.setUsername(securityDTO.getUsername());
            lmshopMember.setPassword(securityDTO.getPassword());
            lmshopMember.setId(Long.parseLong(securityDTO.getId()));
            lmshopMember.setClientId(securityDTO.getClientId());
            if (securityDTO.getMemberState().equals(MEMBER_STATE_OFF.value())) {
                throw new ServiceException("您已被禁用");
            }
        } else {
            log.info("用户不存在创建用户");
            MemberRegisterDTO memberRegisterDTO = new MemberRegisterDTO();
            memberRegisterDTO.setMemberName(s);
            memberRegisterDTO.setMemberMobile(s);
            memberRegisterDTO.setNickName(DataMaskingUtils.mobileEncrypt(s));
            //来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
            if (StringUtils.isNotBlank(memberSource)) {
                memberRegisterDTO.setMemberSource(Integer.valueOf(memberSource));
            } else {
                memberRegisterDTO.setMemberSource(Integer.valueOf(0));
            }
            memberService.saveMember(memberRegisterDTO);
            // 保存结束封装返回参数
            lmshopMember.setUsername(s);
            lmshopMember.setPassword("");
            lmshopMember.setClientId(client);
        }
        return lmshopMember;
    }
}
