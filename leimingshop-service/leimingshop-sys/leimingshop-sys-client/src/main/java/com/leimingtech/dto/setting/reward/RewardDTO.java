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
 * 奖励实体：积分与成长值设置的数值
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/20 11:03
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "RewardDTO")
public class RewardDTO implements Serializable {

    private static final long serialVersionUID = 6116355782918362139L;

    /**
     * 成长值
     */
    @ApiModelProperty(value = "成长值")
    private Integer growthValue;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer pointValue;

}
