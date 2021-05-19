/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.adv;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * 广告类别实体
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "AdvCategoryDTO")
public class AdvCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "广告类别名称")
    @NotBlank(message = "广告类别名称不能为空", groups = AddGroup.class)
    private String categoryName;

    @ApiModelProperty(value = "广告上传提示语")
    private String advTips;

    @NotBlank(message = "广告类别标识不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "广告类别标识")
    private String advKey;

    @ApiModelProperty(value = "广告类别状态 1启用 2停用")
    private Integer status;

    @ApiModelProperty(value = "广告展示类型 0：单图 1：多图")
    private Integer categoryType;

    @ApiModelProperty(value = "广告像素宽（预留字段）")
    private Float advWeidth;

    @ApiModelProperty(value = "广告像素高（预留字段）")
    private Float advHeight;

}
