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
 * 新手任务奖励设置实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:02
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "NoviceTaskSettingDTO")
public class NoviceTaskSettingDTO implements Serializable {

    private static final long serialVersionUID = 6572236106086596575L;

    @ApiModelProperty(value = "新手欢迎奖励")
    private RewardDTO welcome;

    @ApiModelProperty(value = "设置头像")
    private RewardDTO avatar;

    @ApiModelProperty(value = "设置昵称")
    private RewardDTO nikename;

    @ApiModelProperty(value = "完善个人信息")
    private RewardDTO information;

    @ApiModelProperty(value = "首次关注店铺")
    private RewardDTO attentionStore;

    @ApiModelProperty(value = "首次分享商品")
    private RewardDTO sharegoods;

    @ApiModelProperty(value = "首次收藏商品")
    private RewardDTO favoritesgoods;

    @ApiModelProperty(value = "首次购物成功")
    private RewardDTO firstOrder;

    @ApiModelProperty(value = "首次完成评价")
    private RewardDTO evaluateOrder;
}
