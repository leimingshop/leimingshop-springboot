/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.freight.template;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplateDetailDTO;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplatePageDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 运费模板管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */

public interface FreightTemplateService {
    /**
     * 分页查询运费模板
     *
     * @param params 查询条件
     * @return
     */

    PageData<FreightTemplateDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 功能描述：
     * <运营端运费模板分页>
     *
     * @param params 查询条件
     * @return
     * @date 2020/4/21 15:46
     * @author 刘远杰
     **/

    PageData<AdminFreightTemplatePageDTO> adminPage(@RequestParam Map<String, Object> params);

    /**
     * 查询运费模板
     *
     * @param params 查询条件
     * @return 返回运费模信息
     */

    List<FreightTemplateDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述：
     * <店铺运费模板列表>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/4/23 11:54
     * @author 刘远杰
     **/

    List<AdminFreightTemplatePageDTO> storeList(@RequestParam("storeId") Long storeId);

    /**
     * 根据id查询运费模板详情
     *
     * @param id 主键id
     * @return 返回运费模板详情
     */

    FreightTemplateDTO get(Long id);

    /**
     * 功能描述：
     * <运营端运费模板详情>
     *
     * @param id      运费模板id
     * @param storeId 店铺id
     * @return
     * @date 2020/4/22 11:24
     * @author 刘远杰
     **/

    AdminFreightTemplateDetailDTO adminDetail(@RequestParam("id") Long id, @RequestParam("storeId") Long storeId);

    /**
     * 功能描述：
     * <查询店铺指定运费模板>
     *
     * @param id      运费模板id
     * @param storeId 店铺id
     * @return
     * @date 2020/4/22 10:02
     * @author 刘远杰
     **/

    FreightTemplateDTO getStoreFreightTemplateById(@RequestParam("id") Long id, @RequestParam("storeId") Long storeId);

    /**
     * 功能描述：
     * <保存运费模板>
     *
     * @param dto
     * @return
     * @date 2020/4/21 17:52
     * @author 刘远杰
     **/

    void save(@RequestBody FreightTemplateDTO dto);

    /**
     * 功能描述：
     * <保存运费模板>
     *
     * @param dto 保存参数
     * @return
     * @date 2020/4/21 17:52
     * @author 刘远杰
     **/

    Boolean saveFreightTemplate(@RequestBody FreightTemplateDTO dto);

    /**
     * 修改运费模板信息
     *
     * @param dto 修改参数
     */

    void update(@RequestBody FreightTemplateDTO dto);

    /**
     * 功能描述：
     * <修改运费模板>
     *
     * @param dto 修改参数
     * @return
     * @date 2020/4/21 17:52
     * @author 刘远杰
     **/

    Boolean updateFreightTemplate(@RequestBody FreightTemplateDTO dto);

    /**
     * 删除运费模板信息
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述：
     * <删除指定id店铺运费模板>
     *
     * @param ids     运费模板id集合
     * @param storeId 店铺id
     * @return
     * @date 2020/4/22 10:26
     * @author 刘远杰
     **/

    void deleteStoreFreightTemplate(@RequestBody Long[] ids, @RequestParam("storeId") Long storeId);

    /**
     * 功能描述：
     * <更新商品运费模板并删除>
     *
     * @param oldFreightTemplateId
     * @param newFreightTemplateId
     * @param storeId
     * @return
     * @date 2020/4/24 18:03
     * @author 刘远杰
     **/

    void deleteStoreFreightTemplate(@RequestParam("oldFreightTemplateId") Long oldFreightTemplateId,
                                    @RequestParam("newFreightTemplateId") Long newFreightTemplateId,
                                    @RequestParam("storeId") Long storeId);

    /**
     * 功能描述：
     * <修改默认运费模板>
     *
     * @param id          运费模板id
     * @param defaultFlag 是否默认
     * @param storeId     店铺id
     * @return
     * @date 2020/4/22 10:52
     * @author 刘远杰
     **/

    void updateDefaultFreightTemplate(@RequestParam("id") Long id,
                                      @RequestParam("defaultFlag") Integer defaultFlag,
                                      @RequestParam("storeId") Long storeId);

    /**
     * 功能描述 根据模板名称和店铺id 查询模板的id
     *
     * @param freightTemplateName 模板名称
     * @param storeId             店铺id
     * @return java.lang.Long 模板id
     * @author lishuo
     * @date 24/6/2020
     */

    Long findFreightTemplate(@RequestParam("freightTemplateName") String freightTemplateName, @RequestParam("storeId") Long storeId);
}
