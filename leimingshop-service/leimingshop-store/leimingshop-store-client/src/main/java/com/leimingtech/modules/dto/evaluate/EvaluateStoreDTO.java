/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.evaluate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺评分表
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "EvaluateStoreDTO")
public class EvaluateStoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺评价ID")
    private Long id;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "店铺编号")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "买家编号")
    private Long memberId;
    @ApiModelProperty(value = "买家名称")
    private String memberName;
    @ApiModelProperty(value = "描述相符评分")
    private Double sevalDesccredit;
    @ApiModelProperty(value = "服务态度评分")
    private Double sevalServicecredit;
    @ApiModelProperty(value = "发货速度评分")
    private Double sevalDeliverycredit;

}