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
 * @Description 商品审核设置
 * @Date 17:04 2019-05-14
 * @Param
 * @return
 */
@Data
@ApiModel(description = "SettingGoodsAuditDTO")
public class SettingGoodsAuditDTO implements Serializable {

    @ApiModelProperty(value = "商品审核设置开启1是0否", required = true)
    @NotNull
    private String goodsAuditOpen;

    @ApiModelProperty(value = "换货审核设置开启1是0否", required = true)
    @NotNull
    private String goodsExchangeOpen;

    @ApiModelProperty(value = "退货审核设置开启1是0否", required = true)
    @NotNull
    private String goodsReturnOpen;


}
