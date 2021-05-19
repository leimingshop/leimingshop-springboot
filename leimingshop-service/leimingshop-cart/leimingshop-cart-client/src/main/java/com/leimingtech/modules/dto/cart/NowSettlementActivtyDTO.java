/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @Author: 刘远杰
 * @Description: 立即购买结算页优惠信息选择实体
 * @Date: 15:23 2019/12/31
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "NowSettlementActivtyDTO")
public class NowSettlementActivtyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品sku id")
    @NotNull(message = "skuid不能为空", groups = AddGroup.class)
    private Long specId;

    @ApiModelProperty(value = "购买数量")
    @NotNull(message = "购买数量不能为空", groups = AddGroup.class)
    private Integer number;

    @ApiModelProperty(value = "会员id")
    @Null(message = "会员id必须为空", groups = AddGroup.class)
    private Long memberId;

    @ApiModelProperty(value = "会员优惠券id/满减活动id")
    private Long id;

    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    @NotNull(message = "活动类型不能为空", groups = AddGroup.class)
    private Integer activityType;

    @ApiModelProperty(value = "收货地址id")
    private Long addressId;

    @ApiModelProperty(value = "是否使用余额付款 0不使用 1使用")
    private Integer useBalance;

    @ApiModelProperty(value = "拼团记录id(参与其他人拼团时，必传此参数)")
    private Long groupId;


}
