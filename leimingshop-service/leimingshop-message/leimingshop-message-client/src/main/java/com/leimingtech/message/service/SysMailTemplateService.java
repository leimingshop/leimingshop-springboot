/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.message.dto.SysMailTemplateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 邮件模板
 */

public interface SysMailTemplateService {

    /**
     * 发送邮件
     *
     * @param id     邮件模板ID
     * @param mailTo 收件人
     * @param mailCc 抄送
     * @param params 模板参数
     * @return 发送结果
     */

    boolean sendMail(@RequestParam("id") Long id, @RequestParam("mailTo") String mailTo, @RequestParam("mailCc") String mailCc, @RequestParam("params") String params);

    /**
     * 邮件模板列表分页
     *
     * @param params 查询条件
     * @return 查询结果
     */

    PageData<SysMailTemplateDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询邮件模板
     *
     * @param id 模板ID
     * @return SysMailTemplateDTO 查询结果
     */

    SysMailTemplateDTO info(Long id);

    /**
     * 保存邮件模板
     *
     * @param dto 邮件模板保存DTO
     * @return 保存结果
     */

    boolean saveMailTemplate(@RequestBody SysMailTemplateDTO dto);

    /**
     * 修改邮件模板
     *
     * @param dto 邮件模板保存DTO
     * @return 修改结果
     */

    boolean updateMailTemplate(@RequestBody SysMailTemplateDTO dto);

    /**
     * 保存邮件模板
     *
     * @param ids 邮件模板id数组
     * @return 删除结果
     */

    boolean deleteMailTemplates(@RequestBody Long[] ids);


}
