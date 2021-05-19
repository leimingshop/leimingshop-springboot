/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.adv;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 广告类别实体（新增）
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "InsertAdvCategoryDTO")
public class InsertAdvCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "广告类别名称")
    @NotBlank(message = "广告类别名称不能为空", groups = AddGroup.class)
    private String categoryName;

    @ApiModelProperty(value = "广告上传提示语")
    private String advTips;

    @ApiModelProperty(value = "广告类别标识")
    @NotBlank(message = "广告类别标识不能为空", groups = AddGroup.class)
    private String advKey;

    @ApiModelProperty(value = "广告类别状态 1启用 2停用")
    @NotBlank(message = "广告类别状态", groups = AddGroup.class)
    private Integer status;

    @ApiModelProperty(value = "广告像素宽（预留字段）")
    private Float advWeidth;

    @ApiModelProperty(value = "广告像素高（预留字段）")
    private Float advHeight;

    @ApiModelProperty(value = "广告展示类型 0：单图 1：多图")
    private Integer categoryType;

}
