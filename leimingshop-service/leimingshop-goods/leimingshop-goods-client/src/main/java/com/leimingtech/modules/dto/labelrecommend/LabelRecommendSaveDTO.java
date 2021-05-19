/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.labelrecommend;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 标签推荐表(保存实体)
 *
 * @author weixianchun @leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Data
@ApiModel(description = "LabelRecommendSaveDTO")
public class LabelRecommendSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签标识")
    @NotBlank(message = "标签标识不可为空", groups = AddGroup.class)
    private String labelFlag;
    @ApiModelProperty(value = "标签名称")
    @NotBlank(message = "标签名称不可为空", groups = AddGroup.class)
    private String labelName;
    @ApiModelProperty(value = "标签状态（默认1:启用,2:禁用）")
    @NotNull(message = "标签状态不可为空", groups = AddGroup.class)
    private Integer labelStatus;
}
