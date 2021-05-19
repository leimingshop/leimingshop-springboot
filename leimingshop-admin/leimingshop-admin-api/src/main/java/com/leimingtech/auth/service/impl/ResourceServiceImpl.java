/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.auth.service.impl;

import com.leimingtech.auth.jwt.JwtUtils;
import com.leimingtech.auth.service.ResourceService;
import com.leimingtech.commons.tools.enums.SuperAdminEnum;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.UserDetailRedis;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.enums.ResourceAuthEnum;
import com.leimingtech.commons.tools.security.enums.UserKillEnum;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dto.sys.SysUserDTO;
import com.leimingtech.service.sys.SysResourceService;
import com.leimingtech.service.sys.SysRoleDataScopeService;
import com.leimingtech.service.sys.SysUserService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * 资源服务
 *
 * @since 1.0.0
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private UserDetailRedis userDetailRedis;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SysRoleDataScopeService sysRoleDataScopeService;

    @Override
    public UserDetail resource(String token, String url, String method) {
        //1、获取所有资源列表
        List<ResourceBO> resourceList = sysResourceService.getResourceList();

        //2、判断是否在资源列表里
        ResourceBO resource = pathMatcher(url, method, resourceList);

        //3、无需登录认证
        if (resource != null && resource.getAuthLevel() == ResourceAuthEnum.NO_AUTH.value()) {
            return null;
        }

        //4、获取用户信息
        UserDetail userDetail = getUserDetail(token);

        //5、登录认证
        if (resource != null && resource.getAuthLevel() == ResourceAuthEnum.LOGIN_AUTH.value()) {
            return userDetail;
        }

        //6、不在资源列表里，只要登录了，就有权限访问
        if (resource == null) {
            return userDetail;
        }

        //7、当前登录用户是超级管理员
        if (userDetail.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            return userDetail;
        }

        //8、需要鉴权，获取用户资源列表

        List<ResourceBO> userResourceList = userDetail.getResourceList();

        //9、如果不在用户资源列表里，则无权访问
        resource = pathMatcher(url, method, userResourceList);
        if (resource != null) {
            return userDetail;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    /**
     * 根据token，获取用户信息
     *
     * @param token 用户token
     * @return 返回用户信息
     */
    private UserDetail getUserDetail(String token) {
        //token为空
        if (StringUtils.isBlank(token)) {
//            throw new CustomException(ErrorCode.NOT_NULL, Constant.ADMIN_TOKEN_HRADER);
            throw new CustomException("用户登录失效，请刷新页面");
        }

        //是否过期
        Claims claims = jwtUtils.getClaimByToken(token);
        if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        //获取用户ID
        Long userId = Long.parseLong(claims.getSubject());


        //查询Redis，如果没数据，则保持用户信息到Redis
        UserDetail userDetail = userDetailRedis.get(userId);
        if (userDetail == null) {
            //获取用户信息
            SysUserDTO userDTO = sysUserService.get(userId);

            if (userDTO == null) {
                throw new CustomException(ErrorCode.ACCOUNT_NOT_EXIST);
            }
            userDetail = ConvertUtils.sourceToTarget(userDTO, UserDetail.class);

            //用户部门数据权限
            List<Long> deptIdList = sysRoleDataScopeService.getDataScopeList(userDetail.getId());
            userDetail.setDeptIdList(deptIdList);

            //用户资源列表
            List<ResourceBO> resourceList = sysResourceService.getUserResourceList(userDetail.getId());
            userDetail.setResourceList(resourceList);

            //过期时间
            long expire = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
            userDetailRedis.set(userDetail, expire);
        }

        //Redis有数据，则判断是否被踢出，如果被踢出，则提示重新登录
        if (userDetail.getKill() == UserKillEnum.YES.value()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return userDetail;
    }

    private ResourceBO pathMatcher(String url, String method, List<ResourceBO> resourceList) {
        for (ResourceBO resource : resourceList) {
            if (StringUtils.isNotBlank(resource.getResourceUrl()) && antPathMatcher.match(resource.getResourceUrl(), url)
                    && method.equalsIgnoreCase(resource.getResourceMethod())) {
                return resource;
            }
        }
        return null;
    }

}
