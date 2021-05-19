/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 店铺审核表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-20
 */
@Data
@ApiModel(description = "StoreAuthAuditDTO")
public class StoreAuthAuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "法人名称")
    private String legalPersonName;
    @ApiModelProperty(value = "营业执照电子版")
    private String electronicBusinessLicense;
    @ApiModelProperty(value = "公司地址")
    private String companyAddressDetail;
    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayerId;
    @ApiModelProperty(value = "开户行名称")
    private String bankName;
    @ApiModelProperty(value = "开户行账号")
    private String bankAccountNumber;
    @ApiModelProperty(value = "纳税人资格证")
    private String taxpayerPicture;
    @ApiModelProperty(value = "银行开户许可证")
    private String bankAccountPicture;
    @ApiModelProperty(value = "身份证反面")
    private String idCardNatPicture;
    @ApiModelProperty(value = "身份证正面")
    private String idCardPeoPicture;
    @ApiModelProperty(value = "公司信息审核状态(10 待审核 20 审核通过 30 审核拒绝)")
    private Integer authAuditStatus;
    @ApiModelProperty(value = "公司审核备注")
    private String authAuditCause;
    @ApiModelProperty(value = "公司电话")
    private String companyPhone;
    @ApiModelProperty("申请时间")
    private Date createDate;
    @ApiModelProperty("审核时间")
    private Date updateDate;
}