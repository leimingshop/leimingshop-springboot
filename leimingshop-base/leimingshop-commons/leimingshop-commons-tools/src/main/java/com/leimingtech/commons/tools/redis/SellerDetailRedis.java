/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.leimingtech.commons.tools.security.enums.UserKillEnum;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: lixiang
 * @Description: Seller用户Redis
 * @Date: 2019/6/26 14:58
 * @Version: V1.0
 */
@Component
public class SellerDetailRedis {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 设置seller用户缓存
     *
     * @param sellerDetail: seller用户信息
     * @param expire:       用户redis时长
     * @return
     * @date 2019/6/26 14:58
     * @author lixiang
     **/
    public void set(SellerDetail sellerDetail, long expire) {
        if (sellerDetail == null) {
            return;
        }
        String key = RedisKeys.getSecuritySellerKey(sellerDetail.getId());
        //bean转化map便于hash存储
        sellerDetail.setKill(UserKillEnum.NO.value());
        Map<String, Object> map = BeanUtil.beanToMap(sellerDetail, false, true);
        redisUtils.hMSet(key, map, expire);

        //用户登录时，清空菜单导航、权限标识
        redisUtils.delete(RedisKeys.getSellerMenuNavKey(sellerDetail.getId()));
        redisUtils.delete(RedisKeys.getUserPermissionsKey(sellerDetail.getId()));
    }

    /**
     * 从缓存中获取seller登陆信息
     *
     * @param sellerId: sellerID
     * @return 返回Seller用户
     * @date 2019/6/26 15:05
     * @author lixiang
     **/
    public SellerDetail get(Long sellerId) {

        String key = RedisKeys.getSecuritySellerKey(sellerId);

        Map<String, Object> map = redisUtils.hGetAll(key);
        if (MapUtil.isEmpty(map)) {
            return null;
        }

        //map to bean

        return BeanUtil.mapToBean(map, SellerDetail.class, true);
    }

    /**
     * seller退出操作
     *
     * @param sellerId: seller用户Id
     * @date 2019/6/26 15:07
     * @author lixiang
     **/
    public void logout(Long sellerId) {
        String key = RedisKeys.getSecuritySellerKey(sellerId);
        redisUtils.hSet(key, "kill", UserKillEnum.YES.value());

        //清空菜单导航、权限标识
        redisUtils.deleteByPattern(RedisKeys.getSellerMenuNavKey(sellerId));
        redisUtils.delete(RedisKeys.getSellerPermissionsKey(sellerId));
    }

    /**
     * 清空菜单导航、权限标识
     *
     * @param userId
     */
    public void delete(Long userId) {
        //清空菜单导航、权限标识
        redisUtils.deleteByPattern(RedisKeys.getUserMenuNavKey(userId));
        redisUtils.delete(RedisKeys.getUserPermissionsKey(userId));
    }
}