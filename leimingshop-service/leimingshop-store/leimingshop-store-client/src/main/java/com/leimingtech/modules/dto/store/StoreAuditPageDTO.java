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
@ApiModel(description = "StoreAuditPageDTO")
public class StoreAuditPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "管理员账号")
    private String account;
    @ApiModelProperty(value = "店铺简介")
    private String storeIntro;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;
    @ApiModelProperty(value = "店铺联系人电话")
    private String linkmanPhone;
    @ApiModelProperty(value = "店铺联系人")
    private String storeLinkman;
    @ApiModelProperty(value = "普通信息审核状态(10 待审核 20 审核通过 30 审核拒绝)")
    private Integer infoAuditStatus;
    @ApiModelProperty("申请时间")
    private Date createDate;
}