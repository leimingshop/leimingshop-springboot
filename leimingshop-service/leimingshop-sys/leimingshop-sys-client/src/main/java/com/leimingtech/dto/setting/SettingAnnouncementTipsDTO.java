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
 * 公告提示设置
 */
@Data
@ApiModel(description = "SettingAnnouncementTipsDTO")
public class SettingAnnouncementTipsDTO implements Serializable {

    @ApiModelProperty(value = "购物车提示开启设置1是0否", required = true)
    @NotNull
    private String cartTipsOpen;

    @ApiModelProperty(value = "购物车提示内容")
    private String cartTipsContent;

    @ApiModelProperty(value = "订单提示开启设置1是0否", required = true)
    @NotNull
    private String orderTipsOpen;

    @ApiModelProperty(value = "订单提示内容")
    private String orderTipsContent;

}
