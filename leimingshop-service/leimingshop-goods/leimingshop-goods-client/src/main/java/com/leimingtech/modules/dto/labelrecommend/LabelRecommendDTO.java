/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.labelrecommend;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;


/**
 * 标签推荐表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Data
@ApiModel(description = "LabelRecommendDTO")
public class LabelRecommendDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "标签标识")
    @NotBlank(message = "标签标识不可为空", groups = UpdateGroup.class)
    private String labelFlag;
    @ApiModelProperty(value = "标签名称")
    @NotBlank(message = "标签名称不可为空", groups = UpdateGroup.class)
    private String labelName;
    @ApiModelProperty(value = "标签状态（默认1:启用,2:禁用）")
    private Integer labelStatus;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}
