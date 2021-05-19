/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author huangkeyuan
 * @Description 高级设置
 * @Date 17:05 2019-05-14
 * @Param
 * @return
 */
@Data
@ApiModel(description = "SettingSeniorSettlementDTO")
public class SettingSeniorSettlementDTO implements Serializable {

    @ApiModelProperty(value = "统计周期(日结、周结、月结)")
    private String statisticalCycle;

    @ApiModelProperty(value = "是否自动打款1是0否")
    private String automaticMoney;

}
