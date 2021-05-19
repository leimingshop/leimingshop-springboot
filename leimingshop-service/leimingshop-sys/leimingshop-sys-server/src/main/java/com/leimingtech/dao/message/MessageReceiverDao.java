/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.message;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.message.MessageReceiverDTO;
import com.leimingtech.dto.message.SellerMessageReceiverDTO;
import com.leimingtech.entity.message.MessageReceiverEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 短消息接收人表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Mapper
public interface MessageReceiverDao extends BaseDao<MessageReceiverEntity> {

    /**
     * 功能描述:
     * 〈运营端站内信接收人列表〉
     *
     * @param page   分页
     * @param params 参数
     * @author : 刘远杰
     */
    List<MessageReceiverDTO> adminList(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 根据接收者查询总接收信息
     *
     * @param id
     * @return
     */
    int selectByReceiveId(Long id);

    /**
     * 根据接受者获取接收列表
     *
     * @param params
     * @return
     */
    List<MessageReceiverEntity> getList(Map<String, Object> params);

    /**
     * 修改为已读状态
     *
     * @param id
     * @param messageId
     */
    void updateStatus(@Param("id") Long id, @Param("messageId") Long messageId);

    /**
     * 查询seller站内信列表
     *
     * @param page
     * @param params
     * @return
     */
    List<SellerMessageReceiverDTO> sellerMessagePage(IPage<MessageReceiverEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 清除未读消息
     *
     * @param memberId
     */
    void read(Long memberId);

}
