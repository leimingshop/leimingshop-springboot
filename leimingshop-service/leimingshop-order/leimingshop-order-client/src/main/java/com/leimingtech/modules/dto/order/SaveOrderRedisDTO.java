/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <异步保存订单信息>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/11/25
 */
@Data
@ApiModel(description = "SaveOrderRedisDTO")
public class SaveOrderRedisDTO {

    @ApiModelProperty("结果码")
    private Integer resultCode;

    @ApiModelProperty("提示信息")
    private String resultMsg;

    @ApiModelProperty("支付单号")
    private Long paySn;

    @ApiModelProperty("是否拆单 0未拆单 1已拆单")
    private Integer isSplit;

}
