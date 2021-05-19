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
 * 积分规则设置
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:43
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "PointRuleSettingDTO")
public class PointRuleSettingDTO extends BaseRuleSettingDTO implements Serializable {

    private static final long serialVersionUID = -497865748644138071L;

    @ApiModelProperty(value = "清零规则 0不清零，1自然年清零上一年获得的积分 2自然年清零所有积分")
    private Integer clearRule;
}
