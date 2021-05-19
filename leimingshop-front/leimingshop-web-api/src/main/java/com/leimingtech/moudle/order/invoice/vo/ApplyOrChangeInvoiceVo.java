/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.invoice.vo;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.invoice.MemberInvoiceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发票申请、换开对象
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Data
@ApiModel(description = "ApplyOrChangeInvoiceDTO")
public class ApplyOrChangeInvoiceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单ID")
    @NotNull(message = "订单ID不能为空", groups = UpdateGroup.class)
    @NotNull(message = "订单ID不能为空", groups = AddGroup.class)
    private Long orderId;
    @ApiModelProperty(value = "用户提交抬头类型（默认0：不开票、1：个人、2：单位）")
    @NotNull(message = "用户提交抬头类型不能为空", groups = UpdateGroup.class)
    @NotNull(message = "用户提交抬头类型不能为空", groups = AddGroup.class)
    private Integer companyType;
    @ApiModelProperty(value = "用户提交发票类型(1：电子、2：纸质、3：增值税)")
    @NotNull(message = "用户提交发票类型不能为空", groups = UpdateGroup.class)
    @NotNull(message = "用户提交发票类型不能为空", groups = AddGroup.class)
    private Integer memberInvoiceType;
    @ApiModelProperty(value = "用户提交发票内容（1：商品明细、2：商品类别）")
    @NotNull(message = "用户提交发票内容不能为空", groups = UpdateGroup.class)
    @NotNull(message = "用户提交发票内容不能为空", groups = AddGroup.class)
    private Integer memberInvoiceContent;
    @ApiModelProperty(value = "个人名称（个人发票使用）")
    private String personalName;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "收票人名称")
    private String addresseeName;
    @ApiModelProperty(value = "收票人手机")
    private String addresseePhone;
    @ApiModelProperty(value = "收票人邮箱")
    private String addresseeMail;
    @ApiModelProperty(value = "收票人地址（空格分隔）")
    private String addresseeAddress;
    @ApiModelProperty(value = "收票人详细地址")
    private String detailedAddress;
    @ApiModelProperty(value = "省ID")
    private Long provinceId;
    @ApiModelProperty(value = "市ID")
    private Long cityId;
    @ApiModelProperty(value = "区ID")
    private Long districtId;
    @ApiModelProperty(value = "街道ID")
    private Long streetId;
    @ApiModelProperty("企业发票信息（单位开票使用）")
    private MemberInvoiceDTO memberInvoiceDTO;
    @ApiModelProperty(value = "是否换开（默认0：未换开、1：换开）")
    private Integer changeFlag;


}
