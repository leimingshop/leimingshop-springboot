/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <个人中心订单数量>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/11/21
 */
@Data
@ApiModel(description = "MemberOrderCountDTO")
public class MemberOrderCountDTO {

    @ApiModelProperty("待支付数量")
    private Integer paymentNum;

    @ApiModelProperty("待收货数量")
    private Integer receivingNum;

}
