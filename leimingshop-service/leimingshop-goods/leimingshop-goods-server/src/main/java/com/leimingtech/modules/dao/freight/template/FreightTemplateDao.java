/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.freight.template;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplatePageDTO;
import com.leimingtech.modules.entity.freight.template.FreightTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 运费模板管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Mapper
public interface FreightTemplateDao extends BaseDao<FreightTemplateEntity> {

    /**
     * 功能描述：
     * <分页条数>
     *
     * @param params 查询条件
     * @return
     * @date 2020/4/24 11:31
     * @author 刘远杰
     **/
    int countPageTotal(@Param("params") Map<String, Object> params);

    /**
     * 功能描述：
     * <后台运费模板分页>
     *
     * @param page   分页参数
     * @param params 查询条件
     * @return
     * @date 2020/4/21 16:30
     * @author 刘远杰
     **/
    List<AdminFreightTemplatePageDTO> adminPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 功能描述：
     * <店铺运费模板列表>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/4/23 13:22
     * @author 刘远杰
     **/
    List<AdminFreightTemplatePageDTO> storeList(@Param("storeId") Long storeId);

    /**
     * 功能描述 根据模板名称查找模板id
     *
     * @param freightTemplateName 模板的名称
     * @param storeId             店铺的id
     * @return java.lang.Long 模板的id
     * @author lishuo
     * @date 24/6/2020
     */
    Long findFreightTemplate(String freightTemplateName, Long storeId);
}
