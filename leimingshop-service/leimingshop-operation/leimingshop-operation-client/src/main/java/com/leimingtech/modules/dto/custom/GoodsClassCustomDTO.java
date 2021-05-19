/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.custom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品自定义分类表
 *
 * @author xuzhch
 * @email 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@Data
@ApiModel(description = "GoodsClassCustomDTO")
public class GoodsClassCustomDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long gcParentId;

    @ApiModelProperty(value = "展示类目名称")
    private String gcName;

    @ApiModelProperty(value = "展示类目图片")
    private String gcPic;

    @ApiModelProperty(value = "展示类目url")
    private String gcUrl;

    @ApiModelProperty(value = "关联商品分类id")
    private Long classId;

    @ApiModelProperty(value = "层级path。格式：1,2,3")
    private String idPath;

    @ApiModelProperty(value = "前台展示（0不展示，默认为1展示）")
    private Integer showFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}