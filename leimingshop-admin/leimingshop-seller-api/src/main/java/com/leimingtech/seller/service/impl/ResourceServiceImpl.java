/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.service.impl;

import com.leimingtech.commons.tools.enums.SuperSellerEnum;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.SellerDetailRedis;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.enums.ResourceAuthEnum;
import com.leimingtech.commons.tools.security.enums.UserKillEnum;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.modules.dto.user.StoreUserDTO;
import com.leimingtech.modules.service.resource.StoreResourceService;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.seller.jwt.JwtUtils;
import com.leimingtech.seller.service.ResourceService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * 资源服务实现类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/7/16 17:47
 **/
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SellerDetailRedis sellerDetailRedis;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private StoreResourceService storeResourceService;


    /**
     * 获取Seller资源信息
     *
     * @param token:  jwt-token
     * @param url:    请求URL
     * @param method: GET/POST
     * @return 商户ID
     * @date 2019/6/25 18:06
     * @author lixiang
     **/
    @Override
    public SellerDetail resource(String token, String url, String method) {

        // 1、获取所有资源列表
        List<ResourceBO> resourceBOList = storeResourceService.getResourceList();

        //2、判断是否在资源列表里
        ResourceBO resource = pathMatcher(url, method, resourceBOList);

        //3、无需登录认证
        if (resource != null && resource.getAuthLevel() == ResourceAuthEnum.NO_AUTH.value()) {
            return null;
        }


        //token为空
        if (StringUtils.isBlank(token)) {
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
        SellerDetail sellerDetail = sellerDetailRedis.get(userId);
        if (sellerDetail == null) {
            //获取用户信息
            StoreUserDTO storeUserDTO = storeUserService.get(userId);

            if (storeUserDTO == null) {
                throw new CustomException(ErrorCode.ACCOUNT_NOT_EXIST);
            }
            sellerDetail = storeUserService.login(storeUserDTO.getAccount());

        }

        //5、登录认证
        if (resource != null && resource.getAuthLevel() == ResourceAuthEnum.LOGIN_AUTH.value()) {
            return sellerDetail;
        }

        //6、不在资源列表里，只要登录了，就有权限访问
        if (resource == null) {
            return sellerDetail;
        }

        //7、当前登录用户是超级管理员
        if (sellerDetail.getSuperAdmin() == SuperSellerEnum.YES.value()) {
            return sellerDetail;
        }


        //过期时间
        long expire = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
        sellerDetailRedis.set(sellerDetail, expire);

        //Redis有数据，则判断是否被踢出，如果被踢出，则提示重新登录
        if (sellerDetail.getKill() == UserKillEnum.YES.value()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        List<ResourceBO> resourceList = storeResourceService.getUserResourceList(userId);

        //9、如果不在用户资源列表里，则无权访问
        resource = pathMatcher(url, method, resourceList);
        if (resource != null) {
            return sellerDetail;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
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
