/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 标签分组
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Data
@ApiModel(description = "LabelGroupDTO")
public class LabelGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "标签分组名称")
    private String groupName;
    @ApiModelProperty(value = "标签数量")
    private Integer labelNum;
    @ApiModelProperty(value = "标签分组排序")
    private Integer sort;
    @ApiModelProperty(value = "标签分组状态（默认1:启用,2:禁用）")
    private Integer groupStatus;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}
