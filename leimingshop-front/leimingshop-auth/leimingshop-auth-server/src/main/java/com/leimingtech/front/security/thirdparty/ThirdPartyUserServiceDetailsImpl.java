/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.thirdparty;

import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.front.entity.LmshopMember;
import com.leimingtech.modules.enums.member.LoginEnum;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <手机号登陆>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/24
 */
@Service
@Slf4j
public class ThirdPartyUserServiceDetailsImpl implements UserDetailsService {

    private static MemberService mymemberService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void init() {
        mymemberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        log.info("微信用户登录");
        String[] split = mobile.split("-");
        mobile = split[0];
        MemberVoDTO memberVoDTO = mymemberService.getByMobile(mobile);
        // 校验账号是否冻结
        freeze(memberVoDTO.getMemberName());

        if (memberVoDTO.getMemberState().equals(MemberEnum.MEMBER_STATE_OFF.value())) {
            throw new ServiceException("您已被禁用");
        }
        LmshopMember lmshopMember = new LmshopMember();
        if (memberVoDTO != null) {
            lmshopMember.setUsername(memberVoDTO.getMemberName());
            lmshopMember.setId(memberVoDTO.getId());
            lmshopMember.setClientId("client");
        }
        return lmshopMember;
    }

    /**
     * 校验账号是否冻结
     *
     * @param username
     */
    private void freeze(String username) {
        String astrictLoginTimeKey = RedisKeys.getAstrictLoginTimeKey(username);
        Long expire = redisUtils.getExpire(astrictLoginTimeKey);
        int num = -2;
        if (expire == num) {
            return;
        }

        Integer integer = (Integer) redisUtils.get(astrictLoginTimeKey);
        if (integer == LoginEnum.THREE.value()) {
            throw new ServiceException("账号已冻结，请" + expire + "秒后再试");
        } else if (integer == LoginEnum.SIX.value()) {
            throw new ServiceException("账号已冻结，请" + expire / 60 + "分钟后再试");
        } else {
            throw new ServiceException("账号已冻结，请" + expire / 60 / 60 + "小时后再试");
        }
    }
}
