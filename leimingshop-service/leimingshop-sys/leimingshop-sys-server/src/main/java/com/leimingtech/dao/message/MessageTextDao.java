/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.message;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.message.FindMessageTemplateDTO;
import com.leimingtech.dto.message.MessageTextListDTO;
import com.leimingtech.dto.message.SellerMessageTextDTO;
import com.leimingtech.entity.message.MessageTextEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 站内信内容表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Mapper
public interface MessageTextDao extends BaseDao<MessageTextEntity> {

    List<MessageTextListDTO> selectMessageList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 获取短信开关
     *
     * @param type 类型  0站内信 1短信
     * @param code 消息类型
     * @return
     */
    FindMessageTemplateDTO getMessageNo(@Param("type") Integer type, @Param("code") String code);

    /**
     * 查询seller端站内信详情
     *
     * @return
     */
    SellerMessageTextDTO sellerMessageInfo(Long receiveInfoId);

    /**
     * 更新消息已读
     *
     * @param receiveInfoId
     */
    void updateStatus(Long receiveInfoId);

    Integer findCount(@Param("storeId") Long storeId, @Param("type") Integer type);

}
