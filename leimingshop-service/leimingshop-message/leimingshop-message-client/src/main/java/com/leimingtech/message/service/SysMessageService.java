/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.service;


import com.leimingtech.message.dto.MessageDTO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 消息发送记录
 *
 * @author xuzhch
 * @date 2020年5月13日13:56:06
 */

public interface SysMessageService {

    /**
     * 消息发送记录保存方法
     *
     * @param messageDTO 消息内容
     * @author xuzhch
     * @date 2020年9月17日
     */

    void save(@RequestBody MessageDTO messageDTO);

    /**
     * 修改消息发送记录状态
     *
     * @param uniqueMark 消息唯一标识
     * @author xuzhch
     * @date 2020年9月17日
     */

    void sendSuccess(String uniqueMark);

    /**
     * 获取消息发送结果
     *
     * @param uniqueMark 消息唯一标识
     * @return 消息发送结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    String getSendResult(String uniqueMark);
}
