/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.after.template;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.after.template.AfterTemplateDTO;
import com.leimingtech.modules.dto.after.template.AfterTemplateSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * 售后模板
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-17
 */

public interface AfterTemplateService {

    /**
     * 保存售后模板
     *
     * @param dto
     */

    void saveAfterTemple(@RequestBody AfterTemplateSaveDTO dto);

    /**
     * 修改售后模板
     *
     * @param dto
     */

    void update(@RequestBody AfterTemplateDTO dto);

    /**
     * 查询售后模板信息
     *
     * @return com.leimingtech.modules.dto.after.template.AfterTemplateDTO
     * @Description
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 10:52 2019-05-29
     */

    AfterTemplateDTO get(Long id);

    /**
     * 删除售后模板
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 查询分页
     *
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.after.template.AfterTemplateDTO>
     * @Description
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 10:58 2019-05-29
     */

    PageData<AfterTemplateDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询列表
     *
     * @return java.util.List<com.leimingtech.modules.dto.after.template.AfterTemplateDTO>
     * @Description
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 10:58 2019-05-29
     */

    List<AfterTemplateDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述 根据售后模板的名称查询
     *
     * @param afterSaleTemplateName 售后模板名称
     * @return AfterTemplateDTO 售后模板
     * @author lishuo
     * @date 24/6/2020
     */

    AfterTemplateDTO findByName(@RequestParam("afterSaleTemplateName") String afterSaleTemplateName);
}