/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting.reward.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 成长值规则DTO
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:43
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "GrowthRuleSettingDTO")
public class GrowthRuleSettingDTO extends BaseRuleSettingDTO implements Serializable {
    private static final long serialVersionUID = -497865748644138071L;

    @ApiModelProperty(value = "计算周期（月）0代表永久")
    private Integer calculationCycle;

    @ApiModelProperty(value = "计算时间（号）")
    private Integer calculationTime;
}
