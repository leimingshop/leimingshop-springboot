/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <购物车按店铺分组实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/27
 */
@Data
@ApiModel(description = "ActivityCartGroupDTO")
public class ActivityCartGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;
    @ApiModelProperty(value = "活动描述")
    private String activityDescription;
    @ApiModelProperty(value = "活动操作 0以满足全部规则，去逛逛 1已满足部分规则，去凑单 2不满足任何规则，去凑单 3.无满减商品，去逛逛")
    private Integer activityOperation;
    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsAmount;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "最新时间")
    private Long updateDate;

    @ApiModelProperty(value = "活动规则集合")
    private List<ActivityRuleDTO> activityRuleList;

    @ApiModelProperty(value = "当前活动下的购物车集合")
    private List<CartDTO> cartDTOList;

}
