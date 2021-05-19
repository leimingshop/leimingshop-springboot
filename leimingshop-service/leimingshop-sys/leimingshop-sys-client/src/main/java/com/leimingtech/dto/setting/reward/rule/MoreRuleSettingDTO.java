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
 * 更多规则设置
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 18:22
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "MoreRuleSettingDTO")
public class MoreRuleSettingDTO implements Serializable {
    private static final long serialVersionUID = -4722067059531736834L;

    @ApiModelProperty("成长值规则设置")
    private GrowthRuleSettingDTO growthRule;

    @ApiModelProperty("积分规则设置")
    private PointRuleSettingDTO pointRule;
}
