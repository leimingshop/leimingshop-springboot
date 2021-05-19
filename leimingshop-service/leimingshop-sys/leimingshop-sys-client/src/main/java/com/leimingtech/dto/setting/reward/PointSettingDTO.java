/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting.reward;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 积分及成长值设置表
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Data
@ApiModel(description = "PointSettingDTO")
public class PointSettingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "值")
    private String value;
}