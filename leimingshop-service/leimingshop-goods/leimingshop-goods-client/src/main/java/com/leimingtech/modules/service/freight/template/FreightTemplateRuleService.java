/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.freight.template;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.freight.template.FreightTemplateRuleDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 运费模板规则管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */

public interface FreightTemplateRuleService {
    /**
     * 分页查询运费模板
     *
     * @param params 查询参数
     * @return 返回运费模板信息
     */

    PageData<FreightTemplateRuleDTO> page(@RequestParam Map<String, Object> params);

    /***
     * 查询运费模板
     * @param params 查询参数
     * @return 返回运费模板信息
     */

    List<FreightTemplateRuleDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询运费模板详情
     *
     * @param id 主键id
     * @return 返回运费模板详情
     */

    FreightTemplateRuleDTO get(Long id);

    /**
     * 功能描述：
     * <根据运费模板id查询模板规则>
     *
     * @param templateId 运费模板id
     * @return
     * @date 2020/4/22 11:12
     * @author 刘远杰
     **/

    List<FreightTemplateRuleDTO> getByTemplateId(@RequestParam("templateId") Long templateId);

    /**
     * 保存运费模板
     *
     * @param dto 保存参数有
     */

    void save(@RequestBody FreightTemplateRuleDTO dto);

    /**
     * 功能描述：
     * <批量保存运费模板规则>
     *
     * @param dtoList 运费模板规则集合
     * @return
     * @date 2020/4/21 17:46
     * @author 刘远杰
     **/

    Boolean saveBatch(@RequestBody List<FreightTemplateRuleDTO> dtoList);

    /**
     * 修改运费模板
     *
     * @param dto 修改参数
     */

    void update(@RequestBody FreightTemplateRuleDTO dto);

    /**
     * 删除运费模板
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述：
     * <根据模板id物理删除运费模板规则>
     *
     * @param templateId 运费模板id
     * @return
     * @date 2020/4/22 9:41
     * @author 刘远杰
     **/

    Boolean noLogicDeleteByTemplateId(@RequestParam("templateId") Long templateId);
}
