/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.freight.template;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.freight.template.FreightTemplateRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 运费模板规则管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Mapper
public interface FreightTemplateRuleDao extends BaseDao<FreightTemplateRuleEntity> {

    /**
     * 功能描述：
     * <根据运费模板id删除运费模板规则>
     *
     * @param templateId 运费模板id
     * @return
     * @date 2020/4/22 9:43
     * @author 刘远杰
     **/
    int noLogicDeleteByTemplateId(@Param("templateId") Long templateId);

}
