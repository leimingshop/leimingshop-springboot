/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.auditlog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 审核记录表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Data
@ApiModel(description = "AuditLogDTO")
public class AuditLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "操作内容ID")
    private Long contentId;
    @ApiModelProperty(value = "操作类型(1 提交，2 审核)")
    private Integer submitType;
    @ApiModelProperty(value = "审核类型(1 店铺普通信息 2 店铺公司信息 3 店铺入住审核 )")
    private Integer auditType;
    @ApiModelProperty(value = "审核结果(10 提交申请 20 审核通过 30审核拒绝 )")
    private Integer auditStatus;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "操作人")
    private String operator;
    @ApiModelProperty(value = "操作时间")
    private Date createDate;
}