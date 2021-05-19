/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.vo;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 热词设置
 *
 * @author xuzhch
 * @since 1.0.0 2019-12-3
 */
@Data
@ApiModel(description = "HotWordVO")
public class HotWordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "热搜词")
    @Size(message = "30个字以内", max = 30, groups = AddGroup.class)
    private String hotWord;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;
}
