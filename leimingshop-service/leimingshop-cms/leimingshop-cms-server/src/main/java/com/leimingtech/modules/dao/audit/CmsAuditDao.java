/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.audit;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.audit.CmsAuditDTO;
import com.leimingtech.modules.entity.audit.CmsAuditEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 审核记录管理 CmsArticleDao
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2020-03-24
 */
@Mapper
public interface CmsAuditDao extends BaseDao<CmsAuditEntity> {

    /**
     * 审核结果详情
     *
     * @param id
     * @return
     */
    CmsAuditDTO selectAuditInfo(@Param("id") Long id);


}