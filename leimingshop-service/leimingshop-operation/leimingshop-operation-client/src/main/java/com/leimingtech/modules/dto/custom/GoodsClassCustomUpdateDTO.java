/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.custom;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 商品自定义分类表
 *
 * @author xuzhch
 * @email 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@Data
@ApiModel(description = "GoodsClassCustomUpdateDTO")
public class GoodsClassCustomUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long gcParentId;

    @ApiModelProperty(value = "展示类目名称")
    @NotBlank(message = "展示类目名称不能为空", groups = UpdateGroup.class)
    private String gcName;

    @ApiModelProperty(value = "展示类目图片")
    private String gcPic;

    @ApiModelProperty(value = "展示类目url")
    private String gcUrl;

    @ApiModelProperty(value = "关联商品分类id")
    @NotNull(message = "关联商品分类id不能为空", groups = UpdateGroup.class)
    private Long classId;


    @ApiModelProperty("0 H5展示分类， 1 Pc展示分类，默认为0")
    private Integer showType;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}