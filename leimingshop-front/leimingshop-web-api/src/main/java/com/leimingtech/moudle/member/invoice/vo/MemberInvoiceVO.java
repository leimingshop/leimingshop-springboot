/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.invoice.vo;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 用户发票信息
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020年5月18日16:52:25
 */
@Data
@ApiModel(description = "MemberInvoiceVO")
public class MemberInvoiceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键（编辑需要ID，新增不要ID）")
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "企业名称（公司抬头）")
    @NotNull(message = "企业名称不能为空", groups = AddGroup.class)
    @NotNull(message = "企业名称不能为空", groups = UpdateGroup.class)
    private String company;
    @ApiModelProperty(value = "企业税号")
    @NotNull(message = "企业税号不能为空", groups = AddGroup.class)
    @NotNull(message = "企业税号不能为空", groups = UpdateGroup.class)
    private String dutyParagraph;
    @ApiModelProperty(value = "注册地址")
    private String registeredAddress;
    @ApiModelProperty(value = "办公电话")
    private String officePhone;
    @ApiModelProperty(value = "开户银行")
    private String bankName;
    @ApiModelProperty(value = "银行账号")
    @Length(max = 19, message = "银行账号最多允许输入19位", groups = AddGroup.class)
    @Length(max = 19, message = "银行账号最多允许输入19位", groups = UpdateGroup.class)
    private String bankNumber;
    @ApiModelProperty(value = "是否默认（默认0：不默认、1：默认）")
    private Integer defaultFlag;
    @ApiModelProperty(value = "增值税抬头标记（默认：0非增值税抬头、1：增值税发票抬头）")
    private Integer vatFlag;
}