/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.invoice;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商家开票信息
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Data
@ApiModel(description = "IssueOrderInvoiceDTO")
public class IssueOrderInvoiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "发票ID不能为空", groups = AddGroup.class)
    private Long id;
    @ApiModelProperty(value = "商家开具发票类型(1：电子、2：纸质、3：增值税)")
    @NotNull(message = "商家开具发票类型不能为空", groups = AddGroup.class)
    private Integer storeInvoiceType;
    @ApiModelProperty(value = "商家开具发票内容（1：商品明细、2：商品类别）")
    @NotNull(message = "商家开具发票内容不能为空", groups = AddGroup.class)
    private Integer storeInvoiceContent;
    @ApiModelProperty(value = "发票代码")
    @Length(max = 20, message = "发票代码不可超过20个字", groups = AddGroup.class)
    private String invoiceCode;
    @ApiModelProperty(value = "发票号码")
    @Length(max = 20, message = "发票号码不可超过20个字", groups = AddGroup.class)
    private String invoiceNumber;
    @ApiModelProperty(value = "物流公司")
    @Length(max = 20, message = "物流公司名称不可超过20个字", groups = AddGroup.class)
    private String logisticsCompanies;
    @ApiModelProperty(value = "物流单号")
    @Length(max = 20, message = "物流单号不可超过20个字", groups = AddGroup.class)
    private String logisticsNumber;
    @ApiModelProperty(value = "发票文件地址（url）")
    private String fileUrl;

}