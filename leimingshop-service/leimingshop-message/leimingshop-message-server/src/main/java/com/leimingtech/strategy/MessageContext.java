/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy;

import com.leimingtech.message.dto.MessageDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * （消息发送）策略模式上下文角色
 * <p>
 * 通过Spring将实现Strategy的实现类都自动注入到strategyMap类中，
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 9:09
 **/
@Component
public class MessageContext {

    /**
     * 通过Spring将实现BaseMessageStrategy的实现类都自动注入到strategyMap类中，
     * 当用户传入选择的资源池时，自动根据资源池id去对应的资源池实现中查找资源。
     */
    @Resource
    private final Map<String, BaseMessageStrategy> strategyMap = new ConcurrentHashMap<>();

    /**
     * Spring加载bean时执行
     *
     * @param strategyMap 策略Map
     */
    public MessageContext(Map<String, BaseMessageStrategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v) -> this.strategyMap.put(k, v));
    }

    /**
     * 方法执行入口
     *
     * @date 2020/4/22 9:13
     * @author lixiangx@leimingtech.com
     **/
    public void execute(MessageDTO baseMessageDTO) {

        // 循环策略集合取出满足策略的集合
        for (Map.Entry<String, BaseMessageStrategy> entry : strategyMap.entrySet()) {
            BaseMessageStrategy baseMessageStrategy = entry.getValue();
            // 判断是否满足对应的策略
            if (baseMessageStrategy.isSupport(baseMessageDTO.getMessageCode())) {
                baseMessageStrategy.handle(baseMessageDTO);
                // 如果后期存在一个类型满足多个策略  可以移除break
                break;
            }
        }
    }

}
