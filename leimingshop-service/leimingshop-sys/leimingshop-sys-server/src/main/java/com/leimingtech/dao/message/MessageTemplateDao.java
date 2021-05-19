/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.message;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.message.ShopMessageListVO;
import com.leimingtech.entity.message.MessageTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 消息模板配置表
 *
 * @author *** ***@leimingtech.com
 * @since v1.0.0 2019-08-22
 */
@Mapper
public interface MessageTemplateDao extends BaseDao<MessageTemplateEntity> {

    List<ShopMessageListVO> listNoPage();

    void updateIsSend(@Param("param") Map<String, Object> map);

    List<String> selectLabel();

    Integer selectCount(Long messageId);

    void save(@Param("param") Map<String, Object> map);

    void saveApp(@Param("param") Map<String, Object> map);

    void saveSms(@Param("param") Map<String, Object> map);

    void updateByMessageId(@Param("param") Map<String, Object> map);


}