/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 标签分组((修改实体))
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Data
@ApiModel(description = "LabelGroupUpdateDTO")
public class LabelGroupUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "标签分组名称")
    private String groupName;
    @ApiModelProperty(value = "标签分组排序")
    private Integer sort;

    @ApiModelProperty(value = "关联的标签id")
    private Long[] labelIds;
}
