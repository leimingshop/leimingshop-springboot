/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import com.leimingtech.modules.dto.cart.ActivityRuleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <满减活动商品分页列表>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/16
 */
@Data
@ApiModel(description = "ReduceGoodsPageDTO")
public class ReduceGoodsPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("满减活动id")
    private Long activityId;

    @ApiModelProperty("列表商品")
    private List<FrontCouponsGoodsVO> frontCouponsGoodsVOList;

    @ApiModelProperty("规则列表")
    private List<ActivityRuleDTO> ruleDTOList;

    @ApiModelProperty("使用门槛")
    private BigDecimal limitAmount;

    @ApiModelProperty("优惠金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty("商品总金额")
    private BigDecimal goodsAmount;

    @ApiModelProperty("操作类型 0不满足任何规则 1满足部分规则 2满足全部规则")
    private Integer operationType;

    @ApiModelProperty("下一级使用门槛")
    private BigDecimal nextLimitAmount;

    @ApiModelProperty("下一级优惠金额")
    private BigDecimal nextReduceAmount;

    @ApiModelProperty("下一级门槛金额距离")
    private BigDecimal lackAmount;

    @ApiModelProperty("总记录数")
    private Long totalCount;

}
