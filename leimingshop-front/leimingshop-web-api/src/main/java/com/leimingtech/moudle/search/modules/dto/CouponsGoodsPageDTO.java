/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <优惠券活动商品分页列表>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/16
 */
@Data
@ApiModel(description = "CouponsGoodsPageDTO")
public class CouponsGoodsPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券活动id")
    private Long activityId;

    @ApiModelProperty("列表商品")
    private List<FrontCouponsGoodsVO> frontCouponsGoodsVOList;

    @ApiModelProperty("使用门槛")
    private BigDecimal limitAmount;

    @ApiModelProperty("优惠券面额")
    private BigDecimal faceValue;

    @ApiModelProperty("商品总金额")
    private BigDecimal goodsAmount;

    @ApiModelProperty("门槛金额距离")
    private BigDecimal lackAmount;

    @ApiModelProperty("总条数")
    private Long totalCount;

}
