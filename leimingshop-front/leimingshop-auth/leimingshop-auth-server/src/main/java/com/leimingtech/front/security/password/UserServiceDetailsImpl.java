/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.password;

import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.front.entity.LmshopMember;
import com.leimingtech.modules.dto.member.SecurityDTO;
import com.leimingtech.modules.enums.member.LoginEnum;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.modules.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author tyl
 * @Date 2019/5/29 14:46
 * @Description 继承UserDetails 实体类
 **/
@Service
@Slf4j
public class UserServiceDetailsImpl implements UserDetailsService {


    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        // 校验账号是否冻结
        freeze(s);

        SecurityDTO securityDTO = memberService.selectSecurityUserInfo(s);

        Optional.ofNullable(securityDTO).orElseThrow(() -> new ServiceException("用户名密码错误"));

        if (securityDTO.getMemberState().equals(MemberEnum.MEMBER_STATE_OFF.value())) {
            throw new ServiceException("您已被禁用");
        }

        LmshopMember lmshopMember = new LmshopMember();
        lmshopMember.setUsername(securityDTO.getUsername());
        lmshopMember.setPassword(securityDTO.getPassword());
        lmshopMember.setId(Long.parseLong(securityDTO.getId()));
        lmshopMember.setClientId(securityDTO.getClientId());
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
