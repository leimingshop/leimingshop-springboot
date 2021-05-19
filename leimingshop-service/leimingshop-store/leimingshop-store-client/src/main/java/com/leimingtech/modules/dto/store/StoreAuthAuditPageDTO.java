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
 * 店铺公司信息审核表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-20
 */
@Data
@ApiModel(description = "StoreAuthAuditPageDTO")
public class StoreAuthAuditPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "管理员账号")
    private String account;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty("办公电话")
    private String companyPhone;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty(value = "普通信息审核状态(10 待审核 20 审核通过 30 审核拒绝)")
    private Integer authAuditStatus;
    @ApiModelProperty("公司审核备注")
    private String authAuditCause;
    @ApiModelProperty("申请时间")
    private Date createDate;
}