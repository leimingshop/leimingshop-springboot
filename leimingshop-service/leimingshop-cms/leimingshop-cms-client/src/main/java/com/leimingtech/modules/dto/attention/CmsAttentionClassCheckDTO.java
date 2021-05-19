/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attention;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首次进入圈子判断管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "首次进入圈子判断管理")
public class CmsAttentionClassCheckDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "判断（1：首次 0：非首次）")
    private Integer flag;


}
