/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy;

import com.leimingtech.message.dto.MessageDTO;

/**
 * 策略抽象类
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 9:07
 **/
public abstract class BaseMessageStrategy {

    /**
     * 检查该类型是否支持策略
     *
     * @param code 消息类型
     * @return 结果
     * @date 2020/4/22 16:30
     * @author lixiangx@leimingtech.com
     **/
    public abstract boolean isSupport(String code);

    /**
     * 执行方法
     *
     * @param baseMessageDTO 发送消息封装实体
     * @date 2020/4/22 9:14
     * @author lixiangx@leimingtech.com
     **/
    public abstract void handle(MessageDTO baseMessageDTO);

}
