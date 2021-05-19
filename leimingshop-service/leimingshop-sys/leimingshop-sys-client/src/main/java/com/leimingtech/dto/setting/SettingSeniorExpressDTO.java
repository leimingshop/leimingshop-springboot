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
@ApiModel(description = "SettingSeniorExpressDTO")
public class SettingSeniorExpressDTO implements Serializable {

    @ApiModelProperty(value = "快递100key")
    private String expressKey;

    @ApiModelProperty(value = "快递100customer")
    private String expressCustomer;

}
