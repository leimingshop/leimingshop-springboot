/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.strategy;

import java.util.Map;

/**
 * 用户登录校验
 *
 * @author chengqina
 */
public interface LoginCheckStrategy {

    /**
     * 用户信息校验
     *
     * @return 返回校验结果
     * @date 2020/6/4 11:05
     * @author lixiangx@leimingtech.com
     **/
    Map<String, Object> validate();

    /**
     * 检查该类型是否支持策略
     *
     * @param url 请求地址
     * @return 结果
     * @date 2020/4/22 16:30
     * @author lixiangx@leimingtech.com
     **/
    boolean check(String url);
}
