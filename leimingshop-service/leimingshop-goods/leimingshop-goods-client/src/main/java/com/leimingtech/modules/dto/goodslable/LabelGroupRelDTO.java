/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 标签与标签分组关联管理
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Data
@ApiModel(description = "LabelGroupRelDTO")
public class LabelGroupRelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "标签分组ID")
    private Long groupId;
    @ApiModelProperty(value = "标签ID")
    private Long labelId;
}
