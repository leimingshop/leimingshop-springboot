/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.freight.template;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 运费模板规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Data
@ApiModel(description = "FreightTemplateRuleDTO")
public class InsertFreightTemplateRuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "首件（个）/首重（kg）")
    @NotBlank(message = "首件（个）/首重（kg）不能为空", groups = AddGroup.class)
    @Length(min = 1, max = 6, message = "首件（个）/首重（kg）必须为0-999999之间", groups = AddGroup.class)
    private String firstFee;

    @ApiModelProperty(value = "首件（重）运费")
    @NotNull(message = "首件（重）运费不能为空", groups = AddGroup.class)
    @DecimalMin(message = "首件（重）运费不能小于0.01", value = "0.01", groups = AddGroup.class)
    @DecimalMax(message = "首件（重）运费不能大于999999.99", value = "999999.99", groups = AddGroup.class)
    private BigDecimal firstAmount;

    @ApiModelProperty(value = "续件（个）/续重（kg）")
    @NotBlank(message = "续件（个）/续重（kg）不能为空", groups = AddGroup.class)
    @Length(min = 1, max = 6, message = "续件（个）/续重（kg）必须为0-999999之间", groups = AddGroup.class)
    private String additionalFee;

    @ApiModelProperty(value = "续件（重）运费")
    @NotNull(message = "续件（重）运费不能为空", groups = AddGroup.class)
    @DecimalMin(message = "续件（重）运费不能小于0.01", value = "0.01", groups = AddGroup.class)
    @DecimalMax(message = "续件（重）运费不能大于999999.99", value = "999999.99", groups = AddGroup.class)
    private BigDecimal additionalAmount;

    @ApiModelProperty(value = "地区id")
    @NotEmpty(message = "配送区域不能为空", groups = AddGroup.class)
    private List<Long> areaIdsArr;

    @ApiModelProperty(value = "地区描述")
    private String areaDescription;
}
