/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.vo.point;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 积分详情
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */
@Data
@ApiModel(description = "PcPointLogDetailVo")
public class PcPointLogDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户消费积分")
    private Integer consumePoint;
    /**
     * 用户可用积分
     */
    @ApiModelProperty(value = "用户可用积分")
    private Integer availablePoint;

    @ApiModelProperty(value = "累计获得")
    private Integer totalGet;

    @ApiModelProperty(value = "累计使用")
    private Integer totalUsed;

    @ApiModelProperty(value = "支付订单数")
    private Integer payOrders;

    @ApiModelProperty(value = "累计节省")
    private BigDecimal totalSave;
}
