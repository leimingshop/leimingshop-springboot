/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.message;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.message.MessageReceiverDTO;
import com.leimingtech.dto.message.MessageReceiverDeleteDTO;
import com.leimingtech.dto.message.SellerMessageReceiverDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 短消息接收人表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */

public interface MessageReceiverService {
    /**
     * 获取接收人未读条数
     */

    Integer selectByReceiveId(Long id);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */

    MessageReceiverDTO get(Long id);

    /**
     * 接收人保存
     *
     * @param dto
     */

    void save(@RequestBody MessageReceiverDTO dto);

    /**
     * 接收人批量保存
     *
     * @param dtoList
     */

    void saveBatch(@RequestBody List<MessageReceiverDTO> dtoList);

    /**
     * 接收人修改
     *
     * @param dto
     */

    void update(@RequestBody MessageReceiverDTO dto);

    /**
     * 查询用户列表
     *
     * @param params
     * @return
     */

    PageData<MessageReceiverDTO> pageMessag(@RequestParam Map<String, Object> params);

    /**
     * 修改为已读状态
     *
     * @param params
     */

    void updateStatus(@RequestParam Map<String, Object> params);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据接收人id获取列表
     *
     * @param receiveId
     * @return
     */

    List<MessageReceiverDTO> selectListByReceiveId(Long receiveId);

    /**
     * 功能描述:
     * 〈用户删除站内信〉
     *
     * @param messageReceiverDeleteDTO
     * @author : 刘远杰
     */

    Boolean deleteBatch(@RequestBody MessageReceiverDeleteDTO messageReceiverDeleteDTO, @RequestParam("receiveId") Long receiveId);

    /**
     * seller端站内信列表
     *
     * @param params
     * @return
     */

    PageData<SellerMessageReceiverDTO> sellerMessagePage(@RequestParam Map<String, Object> params);

    /**
     * seller端用户删除站内信
     * 《修改展示状态，不修改删除表示》
     *
     * @param ids 接收人表ID
     */

    void deleteShow(@RequestBody Long[] ids);

    /**
     * 批量已读
     *
     * @param ids 主键id
     */

    void batchRead(@RequestBody Long[] ids);

    /**
     * 清除未读消息
     *
     * @param memberId
     */

    void read(@RequestParam("memberId") Long memberId);
}
