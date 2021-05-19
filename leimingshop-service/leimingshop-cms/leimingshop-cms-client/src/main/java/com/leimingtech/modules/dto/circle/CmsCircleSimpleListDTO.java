/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.circle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台圈子管理列表DTO
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2020-01-06
 */
@Data
@ApiModel(value = "圈子管理")
public class CmsCircleSimpleListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 圈子名称
     */
    @ApiModelProperty(value = "圈子名称")
    private String topicName;


}
