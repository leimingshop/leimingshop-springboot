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
 * @version 1.0
 * @Author weixianchun
 * @Description 用户默认头像设置
 * @Date 2019/11/4 16:17
 */
@Data
@ApiModel(description = "SettingDefaultAvatarsDTO")
public class SettingDefaultAvatarsDTO implements Serializable {

    @ApiModelProperty(value = "默认头像", required = true)
    @NotNull
    private String avatar;
}
