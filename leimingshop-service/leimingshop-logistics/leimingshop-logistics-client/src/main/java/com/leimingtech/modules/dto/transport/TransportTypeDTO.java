/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.transport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangtai
 * @date 2019/5/14 0014 20:29
 * @Description:
 */
@Data
@ApiModel(description = "TransportTypeDTO")
public class TransportTypeDTO implements Serializable {

    @ApiModelProperty(value = "快递类型名称")
    private String typeName;
    @ApiModelProperty(value = "快递类型编码")
    private String typeValue;

}
