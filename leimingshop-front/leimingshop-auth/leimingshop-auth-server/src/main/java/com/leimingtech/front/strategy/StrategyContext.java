/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.strategy;

import com.leimingtech.front.strategy.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 策略模式上下文角色
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/30 11:54
 **/
public class StrategyContext {

    /**
     * 所有策略集合
     */
    static List<LoginCheckStrategy> strategyList = new ArrayList<>(8);

    // 初始化策略
    static {
        strategyList.add(new CaptchaStrategy());
        strategyList.add(new MobileStrategy());
        strategyList.add(new PasswordStrategy());
        strategyList.add(new WechatAppStrategy());
        strategyList.add(new WechatH5Strategy());
        strategyList.add(new WechatPcStrategy());
        strategyList.add(new WeiboH5Strategy());
    }

    /**
     * 策略执行入口
     *
     * @date 2020/6/2 18:14
     * @author lixiangx@leimingtech.com
     **/
    public static Map<String, Object> contextMethod(String url) {
        for (LoginCheckStrategy strategy : strategyList) {
            // 判断是否满足对应的策略
            if (strategy.check(url)) {
                return strategy.validate();
            }
        }
        return null;
    }
}
