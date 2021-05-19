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
 * 日常任务奖励设置
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:15
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "DailyTaskSettingDTO")
public class DailyTaskSettingDTO implements Serializable {
    private static final long serialVersionUID = 8607429818364499203L;

    @ApiModelProperty(value = "每日登录")
    private RewardDTO login;

    @ApiModelProperty(value = "每日签到")
    private RewardDTO signin;

    @ApiModelProperty(value = "分享商品")
    private RewardFrequencyDTO shareGoods;

    @ApiModelProperty(value = "邀请好友")
    private RewardFrequencyDTO inviteFriends;

    @ApiModelProperty(value = "好友首次下单成功")
    private RewardDTO friendOrder;
}
