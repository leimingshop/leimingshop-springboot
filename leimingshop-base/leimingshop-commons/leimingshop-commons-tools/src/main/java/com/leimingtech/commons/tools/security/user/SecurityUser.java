/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.security.user;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.redis.SellerDetailRedis;
import com.leimingtech.commons.tools.redis.UserDetailRedis;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 用户
 *
 * @since 1.0.0
 */
@Slf4j
public class SecurityUser {

    private static UserDetailRedis userDetailRedis;
    /**
     * seller用户Redis
     */
    private static SellerDetailRedis sellerDetailRedis;

    static {
        userDetailRedis = SpringContextUtils.getBean(UserDetailRedis.class);
        sellerDetailRedis = SpringContextUtils.getBean(SellerDetailRedis.class);
    }

    private SecurityUser() {
    }

    /**
     * 获取用户信息
     */
    public static UserDetail getUser() {
        Long userId = getUserId();
        if (userId == null) {
            return null;
        }
        return userDetailRedis.get(userId);
    }

    /**
     * 获取登陆用户名信息
     *
     * @return 用户名
     * @date 2019/7/11 11:17
     * @author lixiang
     **/
    public static String getUserName() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        if (request == null) {
            return null;
        }

        try {
            String userName;
            if (ObjectUtils.isEmpty(request.getAttribute(Constant.USER_NAME_KEY))) {
                userName = "System";
            } else {
                userName = request.getAttribute(Constant.USER_NAME_KEY).toString();
            }
            if (StringUtils.isNotBlank(userName)) {
                return URLDecoder.decode(userName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("用户名解码异常:{}", e);
        }
        return null;
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        if (request == null) {
            return null;
        }

        Object obj = request.getAttribute(Constant.USER_KEY);
        if (obj == null) {
            return null;
        }
        String userId = (String) obj;
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        return Long.parseLong(userId);
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        UserDetail user = getUser();
        if (user == null) {
            return null;
        }

        return user.getDeptId();
    }


    /**
     * 获取Seller用户信息
     *
     * @return seller用户基础信息
     * @date 2019/6/26 15:24
     * @author lixiang
     **/
    public static SellerDetail getSeller() {
        Long sellerId = SecurityUser.getSellerId();

        if (sellerId == null) {
            return null;
        }

        return sellerDetailRedis.get(sellerId);
    }

    /**
     * 获取sellerId
     *
     * @return sellerId
     * @date 2019/6/26 15:25
     * @author lixiang
     **/
    private static Long getSellerId() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        if (request == null) {
            return null;
        }

        Object obj = request.getAttribute(Constant.SELLER_USER_KEY);
        if (obj == null) {
            return null;
        }
        String sellerId = (String) obj;
        if (StringUtils.isBlank(sellerId)) {
            return null;
        }
        return Long.parseLong(sellerId);

    }
}