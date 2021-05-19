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
import java.util.List;

/**
 * 前端展示广告分类及展示广告实体
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "AdvCategoryAndAdvDTO")
public class AdvCategoryAndAdvDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "广告类别名称")
    private String categoryName;

    @NotBlank(message = "广告类别标识不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "广告类别标识")
    private String advKey;

    @ApiModelProperty(value = "广告展示类型 0：单图 1：多图")
    private Integer categoryType;

    @ApiModelProperty(value = "广告像素宽（预留字段）")
    private Float advWeidth;

    @ApiModelProperty(value = "广告像素高（预留字段）")
    private Float advHeight;

    @ApiModelProperty(value = "展示广告列表")
    private List<AdvDTO> advList;

}
