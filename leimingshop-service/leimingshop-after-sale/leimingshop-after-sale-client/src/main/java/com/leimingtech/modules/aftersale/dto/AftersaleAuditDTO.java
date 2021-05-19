/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:售后审核保存实体
 * @Date: 2019/6/21 10:20
 * @Version: V1.0
 */
@Data
@ApiModel(description = "AftersaleAuditDTO")
public class AftersaleAuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;

    @ApiModelProperty(value = "审核理由")
    private String reason;

    @ApiModelProperty(value = "审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）")
    private Integer result;

    @ApiModelProperty(value = "审核流程（1:商家审核,2:平台审核）")
    private Long process;

    @ApiModelProperty(value = "审核类型（（0:退货,1:换货,2:仅退款）")
    private Integer aftersaleType;

}