/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 店铺发票设置表
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-27
 */
@Data
@ApiModel(value = "店铺发票设置表")
public class StoreInvoiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "是否允许开具发票（0：不允许，1：允许）")
    @NotNull(message = "是否允许开具发票选项不能为空", groups = UpdateGroup.class)
    @NotNull(message = "是否允许开具发票选项不能为空", groups = AddGroup.class)
    private Integer invoiceFlag;
    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)")
    private String invoiceType;
    @ApiModelProperty(value = "发票内容（多选 1：商品明细、2：商品类别）")
    private String invoiceContent;
    @ApiModelProperty(value = "收件人")
    @NotBlank(message = "收件人不能为空", groups = AddGroup.class)
    @NotBlank(message = "收件人不能为空", groups = UpdateGroup.class)
    private String addressee;
    @ApiModelProperty(value = "收件人电话")
    @NotBlank(message = "收件人电话不能为空", groups = AddGroup.class)
    @NotBlank(message = "收件人电话不能为空", groups = UpdateGroup.class)
    private String addresseePhone;
    @ApiModelProperty(value = "省ID")
    private Long provinceId;
    @ApiModelProperty(value = "市ID")
    private Long cityId;
    @ApiModelProperty(value = "区ID")
    private Long districtId;
    @ApiModelProperty(value = "街道ID")
    private Long streetId;
    @ApiModelProperty(value = "邮寄地址（空格分隔）")
    @Length(max = 50, message = "邮寄地址长度不能超过50", groups = UpdateGroup.class)
    @Length(max = 50, message = "邮寄地址长度不能超过50", groups = AddGroup.class)
    private String mailingAddress;
    @ApiModelProperty(value = "详细地址")
    @Length(max = 50, message = "详细地址长度不能超过50", groups = UpdateGroup.class)
    @Length(max = 50, message = "详细地址长度不能超过50", groups = AddGroup.class)
    private String detailedAddress;
}
