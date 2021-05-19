/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <订单提交结果实体类>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/7/29
 */
@ApiModel(description = "OrderSaveResultDTO")
@Data
public class OrderSaveResultDTO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty(value = "校验是否通过，10:商品与用户认证校验通过，20:商品校验未通过 30用户信息校验未通过 40活动实效 50其他错误")
    private Integer checkStatus;

    @ApiModelProperty(value = "校验未通过的商品")
    private List<ErrorOrderMsgDTO> errorGoodsList;
}

