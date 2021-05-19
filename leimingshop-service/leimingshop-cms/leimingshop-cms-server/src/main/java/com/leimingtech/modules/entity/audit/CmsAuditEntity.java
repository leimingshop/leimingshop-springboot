/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.audit;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 审核记录表
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2020-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_audit")
public class CmsAuditEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 被审核文章ID
     */
    private Long articleId;
    /**
     * 审核结果
     */
    private String auditResult;
}