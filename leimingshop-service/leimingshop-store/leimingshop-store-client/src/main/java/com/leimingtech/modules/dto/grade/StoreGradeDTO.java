/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.grade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺等级表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-30
 */
@Data
@ApiModel(description = "StoreGradeDTO")
public class StoreGradeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "等级名称")
    private String sgName;

    @ApiModelProperty(value = "等级分值")
    private Integer sgGradeScore;

    @ApiModelProperty(value = "允许发布的商品数量(默认为0)")
    private Integer sgGoodsLimit;

    @ApiModelProperty(value = "收费标准")
    private BigDecimal sgPrice;

    @ApiModelProperty(value = "佣金比例")
    private Double brokerageScale;

    @ApiModelProperty(value = "是否启用（0禁用，默认为1启用）")
    private Integer showFlag;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

}