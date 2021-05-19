/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.lock;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zengzh
 */
@Data
@AllArgsConstructor
public class LockInfo {

    /**
     * 锁名称
     */
    private String lockKey;

    /**
     * 锁值
     */
    private String lockValue;

    /**
     * 过期时间
     */
    private Long expire;

    /**
     * 获取锁超时时间
     */
    private Long acquireTimeout;

    /**
     * 获取锁次数
     */
    private int acquireCount = 0;
}
