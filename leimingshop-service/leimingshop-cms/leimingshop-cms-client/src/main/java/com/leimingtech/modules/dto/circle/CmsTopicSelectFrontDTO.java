/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.circle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 圈子话题前台搜索列表DTO
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2020-01-06
 */
@Data
@ApiModel(value = "圈子话题前台搜索列表")
public class CmsTopicSelectFrontDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "话题名称")
    private String topicName;

    @ApiModelProperty(value = "话题logo")
    private String topicLogo;

    @ApiModelProperty(value = "超话标识（0：普话（默认），1：超话）")
    private Integer superTopicFlag;

}
