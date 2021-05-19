/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.redis;

import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.entity.reason.ReasonDescriptionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Data: 2019/11/18 10:32
 * @Author: chengqian
 * @Version: 1.0
 */
@Component
public class SysSettingRedis {

    /**
     * 默认过期时长为24小时，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24L;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 保存系统设置
     *
     * @param paramCode
     * @param paramValue
     */
    public void set(String paramCode, String paramValue) {
        if (paramValue == null) {
            return;
        }
        String sysSettingKey = RedisKeys.getSysSettingKey(null);
        redisUtils.hSet(sysSettingKey, paramCode, paramValue, DEFAULT_EXPIRE);
    }

    /**
     * 获取系统设置
     */
    public Object get(String key) {
        String sysSettingKey = RedisKeys.getSysSettingKey(null);

        return redisUtils.hGet(sysSettingKey, key);
    }


    /**
     * 保存或者修改售后原因
     *
     * @param key
     * @param reasonDescriptionEntity
     */
    public void setAfter(String key, ReasonDescriptionEntity reasonDescriptionEntity) {
        if (reasonDescriptionEntity == null) {
            return;
        }
        String sysSettingKey = RedisKeys.getSysSettingKey(key);
        redisUtils.hSet(sysSettingKey, String.valueOf(reasonDescriptionEntity.getId()), reasonDescriptionEntity, DEFAULT_EXPIRE);

    }

    /**
     * 删除redis缓存
     *
     * @param key
     * @param id
     */
    public void delete(String key, Long[] id) {
        String sysSettingKey = RedisKeys.getSysSettingKey(key);
        redisUtils.hDel(sysSettingKey, id);
    }
}
