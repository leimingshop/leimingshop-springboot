/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.redis;

import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.entity.SysResourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资源管理
 *
 * @since 1.0.0
 */
@Component
public class SysResourceRedis {
    @Autowired
    private RedisUtils redisUtils;

    public void delete() {
        String key = RedisKeys.getSysResourceKey();

        redisUtils.delete(key);
    }

    public void set(List<SysResourceEntity> list) {
        String key = RedisKeys.getSysResourceKey();

        redisUtils.set(key, list);
    }

    public List<SysResourceEntity> get() {
        String key = RedisKeys.getSysResourceKey();

        return (List<SysResourceEntity>) redisUtils.get(key);
    }

}