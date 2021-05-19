/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 售后审核记录表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleAuditLogSaveDTO")
public class AftersaleAuditLogSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "售后审核记录ID不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "售后审核记录ID")
    private Long id;

    @NotNull(message = "售后单号不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;

    @ApiModelProperty(value = "审核理由")
    @Length(max = 500, message = "审核理由最多输入500字", groups = AddGroup.class)
    private String reason;

    @ApiModelProperty(value = "审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）")
    private Integer result;

    @ApiModelProperty(value = "审核流程（1:商家审核,2:平台审核，3：仲裁审核）")
    private Long process;

    @ApiModelProperty(value = "审核类型（0:退货,1:换货,2:仅退款）")
    private Integer aftersaleType;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @ApiModelProperty(value = "更新日期")
    private Date updateDate;

    @ApiModelProperty(value = "创建人")
    private String creator;


}
