/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 售后审核记录
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleAuditLogDTO")
public class AftersaleAuditLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "审核理由")
    private String reason;
    @ApiModelProperty(value = "审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）")
    private Integer result;
    @ApiModelProperty(value = "审核流程（1:商家审核,2:平台审核，3:仲裁审核）")
    private Long process;
    @ApiModelProperty(value = "审核时间")
    private Date createDate;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "更新日期")
    private Date updateDate;
}