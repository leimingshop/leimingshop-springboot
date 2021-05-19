/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "UpdateStoreStatusDTO")
public class UpdateStoreStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty("10 待审核 20 审核通过 30 审核拒绝 40 待提交")
    private Integer registerAuditStatus;
    @ApiModelProperty("审核原因")
    private String registerAuditCause;
    @ApiModelProperty("审核类型 1 店铺普通信息 2 店铺公司信息 3 店铺入住审核")
    private Integer auditType;
    @ApiModelProperty("审核资质或者公司信息ID")
    private Long id;
    /**
     * 发mq使用
     */
    @ApiModelProperty(value = "操作人")
    private String operator;


}