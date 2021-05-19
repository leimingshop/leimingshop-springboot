/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author huangkeyuan
 * @Description 短信设置
 * @Date 17:05 2019-05-14
 * @Param
 * @return
 */
@Data
@ApiModel(description = "SettingMessageDTO")
public class SettingMessageDTO implements Serializable {

    @ApiModelProperty(value = "发货短信开启设置1是0否", required = true)
    @NotNull
    private String shipmentsIsOpen;

    @ApiModelProperty(value = "退货短信开启设置1是0否", required = true)
    @NotNull
    private String refundIsOpen;
}
