/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.transport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 物流信息表
 *
 * @author songwenhao
 * @since v1.0.0 2019-08-13
 */
@Data
@ApiModel(description = "TransportProcessDTO")
public class TransportProcessDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物流详细信息")
    private String context;
    @ApiModelProperty(value = "物流进度时间")
    private String time;
    @ApiModelProperty(value = "物流进度时间（格式化后）")
    private String ftime;
}