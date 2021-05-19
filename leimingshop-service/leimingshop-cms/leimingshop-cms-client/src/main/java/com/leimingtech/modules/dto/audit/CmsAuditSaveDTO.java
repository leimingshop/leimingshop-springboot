/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.audit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 审核新增管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "审核新增管理")
public class CmsAuditSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "审核标识（0：未审核（默认），1：审核通过，2：审核驳回）")
    private Integer audit;

    @ApiModelProperty(value = "审核结果")
    private String auditResult;

}