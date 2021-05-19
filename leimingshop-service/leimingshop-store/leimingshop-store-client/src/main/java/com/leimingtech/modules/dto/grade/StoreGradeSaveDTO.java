/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.grade;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
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
@ApiModel(description = "StoreGradeSaveDTO")
public class StoreGradeSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "等级名称")
    @NotBlank(message = "等级名称不能为空", groups = AddGroup.class)
    private String sgName;

    @ApiModelProperty(value = "允许发布的商品数量(默认为0)")
    @NotNull(message = "允许发布的商品数量不能为空", groups = AddGroup.class)
    private Integer sgGoodsLimit;

    @ApiModelProperty(value = "收费标准")
    @NotNull(message = "收费标准不能为空", groups = AddGroup.class)
    private BigDecimal sgPrice;

    @ApiModelProperty(value = "佣金比例")
    @NotNull(message = "佣金比例不能为空", groups = AddGroup.class)
    @Digits(message = "佣金比率取值范围为0.01-100.00", integer = 3, fraction = 2, groups = AddGroup.class)
    @Min(message = "佣金比率取值范围为0.01-100.00", value = 0, groups = AddGroup.class)
    @Max(message = "佣金比率取值范围为0.01-100.00", value = 100, groups = AddGroup.class)
    private Double brokerageScale;

    @ApiModelProperty(value = "是否启用（0禁用，默认为1启用）")
    private Integer showFlag;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

}