/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 功能描述：
 * <订单实况信息>
 *
 * @author chengqian
 * @version 1.0
 * @Date 2019/6/17 18:17
 **/
@Data
@ApiModel(description = "OrderLiveDTO")
public class OrderLiveDTO {

    @ApiModelProperty("今日订单总数")
    private Integer orderCount;

    @ApiModelProperty("待支付订单")
    private Integer unpaidCount;

    @ApiModelProperty("待发货订单")
    private Integer pendingCount;

    @ApiModelProperty("待待审核订单")
    private Integer toAuditCount;

    @ApiModelProperty(name = "创建时间")
    private Date createDate;

}
