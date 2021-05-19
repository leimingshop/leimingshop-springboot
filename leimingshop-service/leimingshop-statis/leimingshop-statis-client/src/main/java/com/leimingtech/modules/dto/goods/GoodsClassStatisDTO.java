/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分类统计数据对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(description = "GoodsClassStatisDTO")
public class GoodsClassStatisDTO implements Serializable {
    @ApiModelProperty(value = "一级分类ID")
    private Long firstGcId;

    @ApiModelProperty(value = "分类名称")
    private String className;

    @ApiModelProperty(value = "销售数量")
    private Integer salesNumber;

    @ApiModelProperty(value = "销售金额")
    private BigDecimal salesAmount;

    @ApiModelProperty(value = "销售数量比例")
    private Double salesNumberProportion;

    @ApiModelProperty(value = "销售金额比例")
    private Double salesAmountProportion;

    @ApiModelProperty(value = "创建时间（日）")
    private Date createDayTime;

    public static GoodsClassStatisDTO newGoodsClassStatisDTO() {
        GoodsClassStatisDTO goodsClassStatisDTO = new GoodsClassStatisDTO();
        goodsClassStatisDTO.setSalesNumberProportion(new Double(0));
        goodsClassStatisDTO.setSalesAmountProportion(new Double(0));
        goodsClassStatisDTO.setSalesNumber(0);
        goodsClassStatisDTO.setSalesAmount(BigDecimal.ZERO);
        return goodsClassStatisDTO;
    }

}
