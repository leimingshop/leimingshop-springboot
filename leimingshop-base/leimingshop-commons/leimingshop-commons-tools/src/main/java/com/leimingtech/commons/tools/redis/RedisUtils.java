/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @Update： LX 增加方式注释
 * @since 1.0.0
 */
public class RedisUtils {

    /**
     * 默认过期时长为24小时，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**
     * 过期时长为1小时，单位：秒
     */
    public static final long HOUR_ONE_EXPIRE = 60 * 60 * 1L;
    /**
     * 过期时长为2小时，单位：秒
     */
    public static final long HOUR_TWO_EXPIRE = 60 * 60 * 2L;
    /**
     * 过期时长为6小时，单位：秒
     */
    public static final long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**
     * 过期时长为1分钟，单位：秒
     */
    public static final long MIN_ONE_EXPIRE = 60L;
    /**
     * 不设置过期时长
     */
    public static final long NOT_EXPIRE = -1L;
    /**
     * 获取锁过期时间  1h
     */
    public static final int LOCK_EXPIRE = 1 * 60 * 60 * 1000;
    private RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param key:    redis中的Key
     * @param value:  redis中的value
     * @param expire: 过期时间（单位秒）
     * @Author: LX 17839193044@162.com
     * @Description: 数据保存到Redis中
     * @Date: 2019/5/20 9:47
     * @Update: 增加注释
     * @Version: V1.0
     */
    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * @param key:   Redis的key
     * @param value: 数据
     * @Author: LX 17839193044@162.com
     * @Description: 使用默认过期时间存储数据
     * @Date: 2019/5/20 9:48
     * @Version: V1.0
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * @param key:    redis key
     * @param expire: 过期时间
     * @Author: LX 17839193044@162.com
     * @Description: 获取缓存数据，并设置过期时间
     * @Date: 2019/5/20 9:58
     * @Version: V1.0
     */
    public Object get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
        return value;
    }

    /**
     * @param key: redis key
     * @Author: LX 17839193044@162.com
     * @Description: 直接获取缓存数据
     * @Date: 2019/5/20 10:16
     * @Version: V1.0
     */
    public Object get(String key) {
        return get(key, NOT_EXPIRE);
    }

    /**
     * @param pattern: 匹配关键词
     * @Author: LX 17839193044@162.com
     * @Description: Redis模糊匹配
     * @Date: 2019/5/20 10:17
     * @Version: V1.0
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * @param pattern: 匹配关键词
     * @Author: LX 17839193044@162.com
     * @Description: 删除模糊匹配缓存
     * @Date: 2019/5/20 10:18
     * @Version: V1.0
     */
    public void deleteByPattern(String pattern) {
        redisTemplate.delete(keys(pattern));
    }

    /**
     * @param key: redis key
     * @Author: LX 17839193044@162.com
     * @Description: 根据Key删除缓存数据
     * @Date: 2019/5/20 10:18
     * @Version: V1.0
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * @param keys: key集合
     * @Author: LX 17839193044@162.com
     * @Description: 根据key集合删除缓存数据
     * @Date: 2019/5/20 10:18
     * @Version: V1.0
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * @param key:   hash key
     * @param field: 指定的field
     * @Author: LX 17839193044@162.com
     * @Description: 获取hash类型中指定field，有则返回，没有返回null
     * @Date: 2019/5/20 10:27
     * @Version: V1.0
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * @param key: hash key
     * @Author: LX 17839193044@162.com
     * @Description: 获取hash key下面全部的数据
     * @Date: 2019/5/20 10:50
     * @Version: V1.0
     */
    public Map<String, Object> hGetAll(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    /**
     * @param key: hash key
     * @param map: 数据
     * @Author: LX 17839193044@162.com
     * @Description: 存放Hash类型的缓存数据（设置默认过期时间24H）
     * @Date: 2019/5/20 10:51
     * @Version: V1.0
     */
    public void hMSet(String key, Map<String, Object> map) {
        hMSet(key, map, DEFAULT_EXPIRE);
    }

    /**
     * @param key:    hash key
     * @param map:    数据
     * @param expire: 过期时间（秒）
     * @Author: LX 17839193044@162.com
     * @Description: 存放Hash类型的缓存数据
     * @Date: 2019/5/20 10:51
     * @Version: V1.0
     */
    public void hMSet(String key, Map<String, Object> map, long expire) {
        redisTemplate.opsForHash().putAll(key, map);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }

    }

    /**
     * @param key:   hash key
     * @param field: field名称
     * @param value: 数据
     * @Author: LX 17839193044@162.com
     * @Description: 修改Hash类型缓存指定field的值
     * @Date: 2019/5/20 10:53
     * @Version: V1.0
     */
    public void hSet(String key, String field, Object value) {
        hSet(key, field, value, DEFAULT_EXPIRE);
    }

    public void hSet(String key, String field, Object value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * @param key:    redis key
     * @param expire: 过期时间（单位秒）
     * @Author: LX 17839193044@162.com
     * @Description: 设置缓存过期时间
     * @Date: 2019/5/20 9:54
     * @Version: V1.0
     */
    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }


    /**
     * @param key:    hash主键
     * @param fields: 可变参数的field
     * @Author: LX 17839193044@162.com
     * @Description: 删除指定filed数据
     * @Date: 2019/5/20 10:55
     * @Version: V1.0
     */
    public void hDel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public void leftPush(String key, Object value) {
        leftPush(key, value, DEFAULT_EXPIRE);
    }

    public void leftPush(String key, Object value, long expire) {
        redisTemplate.opsForList().leftPush(key, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取分布式锁
     *
     * @param key key值
     * @return 是否获取到
     */
    public boolean tryLock(String key) {
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(key.getBytes(), String.valueOf(expireAt).getBytes());

            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(key.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));

                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(key.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 释放锁
     *
     * @param key key值
     * @return 是否获取到
     */
    public boolean releaseLock(String key) {
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            byte[] value = connection.get(key.getBytes());

            //判断key是否存在，不存在说明已经删除
            if (Objects.nonNull(value) && value.length > 0) {

                long expireTime = Long.parseLong(new String(value));
                //判断过期时间是否大于当前时间，如果过期时间没到，手动删除key
                if (expireTime >= System.currentTimeMillis()) {
                    connection.del(key.getBytes());
                }
            }
            return true;
        });
    }

    /**
     * @param key:
     * @return java.lang.Long
     * @Description 获取过期时间
     * @Author huangkeyuan
     * @Date 17:38 2019-12-19
     */
    public Long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire;
    }

    /**
     * 减一操作（原子性）
     *
     * @param key: Reids key
     * @return 操作后的结果
     * @date 2020/3/9 13:53
     * @author lixiangx@leimingtech.com
     **/
    public Long decrement(String key, int num) throws Exception {
        Boolean result = redisTemplate.hasKey(key);
        if (result == null || !result) {
            throw new Exception("缓存数据不存在");
        }
        return redisTemplate.opsForValue().increment(key, -num);
    }


    /**
     * 加一操作（原子性）
     *
     * @param key: Reids key
     * @return 操作后的结果
     * @date 2020/3/9 13:53
     * @author lixiangx@leimingtech.com
     **/
    public Long increment(String key, int num) throws Exception {
        Boolean result = redisTemplate.hasKey(key);
        if (result == null || !result) {
            throw new Exception("缓存数据不存在");
        }
        return redisTemplate.opsForValue().increment(key, num);
    }

    /**------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

}
