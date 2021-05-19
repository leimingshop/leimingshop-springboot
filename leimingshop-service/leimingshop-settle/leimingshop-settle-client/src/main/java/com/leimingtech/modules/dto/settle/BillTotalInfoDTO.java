/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.settle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 对账详情
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@ApiModel(description = "BillTotalInfoDTO")
public class BillTotalInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "对账汇总信息")
    private BillTotalDTO billTotalDTO;
    @ApiModelProperty("订单结算信息")
    private List<OrderBillDTO> orderBillDTO;
    @ApiModelProperty("退款结算信息")
    private List<ReturnBillDTO> returnBillDTO;
    @ApiModelProperty("操作记录")
    private List<BillLogDTO> list;
}