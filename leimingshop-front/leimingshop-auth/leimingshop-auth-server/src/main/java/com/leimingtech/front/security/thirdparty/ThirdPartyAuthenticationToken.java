/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.thirdparty;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <第三方登陆、微信登录>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/23
 */
public class ThirdPartyAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 256L;
    private final Object principal;

    ThirdPartyAuthenticationToken(String mobile) {
        //因为刚开始并没有认证，因此用户没有任何权限，并且设置没有认证的信息（setAuthenticated(false)）
        super(null);
        //这里的principal就是手机号
        this.principal = mobile;
        this.setAuthenticated(false);
    }

    ThirdPartyAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
