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
 * 奖励次数实体：积分与成长值设置的数值以及次数限制
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:03
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "RewardFrequencyDTO")
public class RewardFrequencyDTO extends RewardDTO implements Serializable {

    private static final long serialVersionUID = -1580035511115678112L;

    @ApiModelProperty("次数")
    private Integer frequency;
}
