/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order.freight;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 修改运费规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-22
 */
@Data
@ApiModel(description = "EditFreightRuleDTO")
public class EditFreightRuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则类型：0叠加运费 1最高运费 2最低运费 3智能组合")
    @NotNull(message = "运费规则不能为空", groups = UpdateGroup.class)
    private Integer ruleType;

}
