/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods.group;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新增拼团活动商品sku实体
 *
 * @author keyuan
 * @since v1.0.0 2020-03-11
 */
@Data
@ApiModel(value = "新增拼团活动商品sku实体")
public class InsertGroupSkuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品sku id")
    @NotNull(message = "商品sku id不能为空", groups = AddGroup.class)
    private Long specId;

    @ApiModelProperty(value = "活动库存")
    @NotNull(message = "活动库存不能为空", groups = AddGroup.class)
    private Integer activityStorage;

    @ApiModelProperty(value = "活动价格")
    @DecimalMin(message = "活动价格不能小于0.01", value = "0.01", groups = AddGroup.class)
    @DecimalMax(message = "活动价格不能大于99999999.99", value = "99999999.99", groups = AddGroup.class)
    @NotNull(message = "活动价格不能为空", groups = AddGroup.class)
    private BigDecimal activityPrice;

}
