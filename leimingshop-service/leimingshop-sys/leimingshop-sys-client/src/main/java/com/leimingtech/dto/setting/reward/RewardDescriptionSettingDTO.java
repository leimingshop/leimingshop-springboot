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
 * 奖励说明设置实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:27
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "RewardDescriptionSettingDTO")
public class RewardDescriptionSettingDTO implements Serializable {
    private static final long serialVersionUID = -1843233996510772215L;

    @ApiModelProperty("积分获取说明")
    private String pointDescription;

    @ApiModelProperty("成长值获取说明")
    private String growthDescription;
}
