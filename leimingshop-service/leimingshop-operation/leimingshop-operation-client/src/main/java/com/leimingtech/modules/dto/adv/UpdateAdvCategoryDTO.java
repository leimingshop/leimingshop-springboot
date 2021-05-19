/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.adv;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 广告类别实体(修改)
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "UpdateAdvCategoryDTO")
public class UpdateAdvCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "广告上传提示语")
    @NotBlank(message = "广告上传提示语不能为空", groups = UpdateGroup.class)
    private String advTips;

    @ApiModelProperty(value = "广告展示类型 0：单图 1：多图")
    @NotNull(message = "广告上传提示语不能为空", groups = UpdateGroup.class)
    private Integer categoryType;

    @ApiModelProperty(value = "广告类别状态 1启用 2停用")
    @NotNull(message = "广告类别状态不能为空", groups = UpdateGroup.class)
    private Integer status;

}
