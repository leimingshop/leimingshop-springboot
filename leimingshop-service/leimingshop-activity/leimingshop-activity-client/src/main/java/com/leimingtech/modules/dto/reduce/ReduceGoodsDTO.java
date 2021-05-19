/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.reduce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 满减活动商品实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "ReduceGoodsDTO")
public class ReduceGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 满减商品id (映射)
     */
    private Long reduceGoodsId;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "优惠券活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;
    @ApiModelProperty(value = "关联id")
    private Long relationId;
    @ApiModelProperty(value = "关联名称")
    private String relationName;
}
