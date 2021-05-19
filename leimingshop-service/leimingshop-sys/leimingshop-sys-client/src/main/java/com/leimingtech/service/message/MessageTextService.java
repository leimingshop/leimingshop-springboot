/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.message;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.message.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 站内信内容表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */

public interface MessageTextService {

    /**
     * 站内信保存
     *
     * @param messageTextDTO
     * @return
     */

    void saveMessage(@RequestBody MessageTextDTO messageTextDTO);

    /**
     * 分页查询  可根据标题查询
     *
     * @param params
     * @return
     */

    PageData<MessageTextListDTO> pageMessage(@RequestParam Map<String, Object> params);

    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<MessageMongoDTO> pageList(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询站内信详情
     *
     * @param id
     * @return
     */

    MessageTextDTO get(Long id);

    /**
     * 根据ID获取信息内容
     */

    MessageMongoDTO getMessageInfo(@RequestParam("id") Long id);

    /**
     * 修改站内信
     *
     * @param dto
     */

    void update(@RequestBody MessageTextDTO dto);


    /**
     * 站内信批量删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据接收人id获取站内信列表
     *
     * @param params
     * @return
     */

    PageData<MessageTextListDTO> pageByReceiveId(@RequestParam Map<String, Object> params);

    /**
     * 获取短信开关
     *
     * @param type 类型  0 站内信 1 短信 4 站内信、短信、微信消息推送
     * @param code 消息类型
     * @return
     */

    FindMessageTemplateDTO getMessageNo(@RequestParam("type") Integer type,
                                        @RequestParam("code") String code);

    /**
     * 查询seller端站内信详情
     *
     * @param receiveInfoId 站内信接收人表ID
     * @return
     */

    SellerMessageTextDTO sellerMessageInfo(@RequestParam("receiveInfoId") Long receiveInfoId);

    /**
     * 获取消息提醒列表
     *
     * @param storeId
     */

    MessageRemindDTO getRemingList(@RequestParam("storeId") Long storeId);


    void deleteInnerMessage(@RequestBody Long[] ids);
}
