/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 售后日志表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-06-23
 */
@Data
@ApiModel(description = "AftersaleRefundRecordsDTO")
public class AftersaleRefundRecordsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "退款状态(1:待审核,2:待退款,3:退款中,4:退款成功,5:退款失败)")
    private Integer refundStatus;
}