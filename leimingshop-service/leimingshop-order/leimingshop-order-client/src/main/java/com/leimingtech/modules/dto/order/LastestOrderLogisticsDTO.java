/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 最新订单物流信息
 * @Date: 2019/8/14 21:38
 * @Version: V1.0
 */
@ApiModel(description = "LastestOrderLogisticsDTO")
@Data
public class LastestOrderLogisticsDTO {

    @ApiModelProperty(value = "商品图片")
    private String goodsPicture;
    @ApiModelProperty(value = "最新物流新信息")
    private String context;
    @ApiModelProperty(value = "快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回")
    private Integer state;
    @ApiModelProperty(value = "订单号")
    private Long orderId;
}

