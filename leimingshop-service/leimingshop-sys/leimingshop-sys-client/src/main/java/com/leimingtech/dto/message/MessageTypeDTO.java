/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/8/23 14:10
 * @Description
 **/
@Data
@ApiModel(description = "MessageTypeDTO")
public class MessageTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "消息类型名称")
    private String messageTypeName;

}
