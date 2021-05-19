/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 仲裁审核保存
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "仲裁审核保存")
public class AuditArbitrationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "仲裁状态（1:审核通过、2:审核不通过、默认：3:待审核）")
    private Integer arbitrationStatus;
    @ApiModelProperty(value = "平台审核理由")
    private String auditReason;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "审核人")
    private String auditor;
    @ApiModelProperty(value = "审核日志表ID")
    private Long auditLogId;

}
