/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.security;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * @Author tyl
 * @Date 2019/6/15 15:43
 * @Description 获取登陆用户信息和用户名
 **/
@Slf4j
public class SecurityCurrentUser {

    private static RedisUtils redisUtils;

    static {
        redisUtils = SpringContextUtils.getBean(RedisUtils.class);
    }

    private SecurityCurrentUser() {
    }

    /**
     * 判断用户是否登陆
     */
    public static boolean userIsLogin() {
        return (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken));

    }

    /**
     * 经过测试 security 没有的登陆获取的是anonymousUser ，isAuthenticated()方法会返回true
     * 获取用户id
     *
     * @return
     */
    public static String getUserName() {
        if (!userIsLogin()) {
            log.error("security用户名空,没有认证");
            throw new CustomException(MemberErrorEnum.E_USERLOGIN_CODE_ERROR.code(), MemberErrorEnum.E_USERLOGIN_CODE_ERROR.value());
        } else {
            Object obj = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            LmshopMember lmshopMember = JSON.parseObject(JSON.toJSONString(obj), LmshopMember.class);
            return lmshopMember.getUsername();
        }

    }

    /***
     *
     * 获取用户信息
     * @return
     */
    public static MemberDTO getUserDetail() {
        String username = getUserName();
        Object object = redisUtils.get(RedisKeys.getSecurityUserKey(username));
        if (object == null) {
            throw new CustomException(MemberErrorEnum.E_USERLOGIN_CODE_ERROR.code(), MemberErrorEnum.E_USERLOGIN_CODE_ERROR.value());
        }
        return (MemberDTO) object;
    }

}