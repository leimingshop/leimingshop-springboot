/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.message;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.message.FindMessageTemplateDTO;
import com.leimingtech.dto.message.ShopMessageListVO;
import com.leimingtech.dto.message.ShopMessageTemplateVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 消息模板配置表
 *
 * @author tyl@leimingtech.com
 * @since v1.0.0 2019-08-22
 */

public interface MessageTemplateService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     */

    PageData<ShopMessageTemplateVO> page(@RequestParam Map<String, Object> params);


    /**
     * 获取结果集
     *
     * @param params
     * @return
     */

    List<ShopMessageTemplateVO> list(@RequestParam Map<String, Object> params);

    /**
     * 无分页消息列表
     *
     * @return
     */

    List<ShopMessageListVO> listNoPage();

    /**
     * 获取实体信息
     *
     * @param id
     * @return
     */

    ShopMessageTemplateVO get(Long id);

    /**
     * 获取模板标签信息
     *
     * @param id
     * @return
     */

    Map<String, Object> getTemplate(Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody ShopMessageTemplateVO dto);

    /**
     * 模板新增或更新
     *
     * @param params
     */

    void saveOrUpdate(@RequestParam Map<String, Object> params);

    /**
     * 修改
     *
     * @param dto
     */

    void update(@RequestBody ShopMessageTemplateVO dto);

    /**
     * 修改发送短信
     *
     * @param map
     */

    void updateIsSend(@RequestParam Map<String, Object> map);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据code获取消息模板
     *
     * @param code: 消息code
     * @return 消息模板
     */

    FindMessageTemplateDTO getMessageByCode(@RequestParam("code") String code);
}