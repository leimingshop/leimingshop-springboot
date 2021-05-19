/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting.reward;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 奖励设置
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:31
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "RewardSettingDTO")
public class RewardSettingDTO implements Serializable {
    private static final long serialVersionUID = 2286741328463384990L;

    @ApiModelProperty(value = "新手任务奖励设置")
    private NoviceTaskSettingDTO noviceTaskSetting;

    @ApiModelProperty(value = "日常任务奖励设置")
    private DailyTaskSettingDTO dailyTaskSetting;

    @ApiModelProperty(value = "奖励领取说明")
    private RewardDescriptionSettingDTO rewardDescription;

}
