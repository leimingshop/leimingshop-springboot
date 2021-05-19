/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.constants;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 系统设置专用redis
 * @Date: 2019/7/19 13:52
 * @Version: V1.0
 */
public class SysRedisConstants {

    /**
     * 地址缓存
     */
    public static final String AREA = "area:";
    /**
     * 地址树结构缓存
     */
    public static final String AREA_TREE = "area:tree:";
    /**
     * 大区地址缓存
     */
    public static final String REGIONANDAREA = "regionandarea:";
    /**
     * 地址缓存压缩版
     */
    public static final String AREA_MINI = "areamini:";
    /**
     * 地址版本缓存
     */
    public static final String AREA_VERSION = "areaversion:";
    /**
     * 地址版本缓存压缩版
     */
    public static final String AREA_MINI_VERSION = "areaminiversion:";
    /**
     * 地址版本缓存
     */
    public static final String REDION_AREA_VERSION = "regionareaversion:";
    /**
     * 缓存默认时间
     */
    public static final int JEDIS_EXPIRE = 6000000;

    private SysRedisConstants() {
    }

}
