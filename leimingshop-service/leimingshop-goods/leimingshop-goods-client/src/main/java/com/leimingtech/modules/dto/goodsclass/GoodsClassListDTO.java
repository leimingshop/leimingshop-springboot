/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodsclass;

import com.leimingtech.commons.tools.utils.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 商品自定义分类表
 *
 * @author huangkeyuan
 * @email
 * @since 1.0.0 2019-05-22
 */
@Data
@ApiModel(description = "GoodsClassListDTO")
public class GoodsClassListDTO extends TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long gcParentId;

    @ApiModelProperty(value = "分类名称")
    private String gcName;

    @ApiModelProperty(value = "是否可以新增下级")
    private Boolean lowerLevel;

    @ApiModelProperty(value = "分类类型（0:实体商品分类，1:虚拟商品分类）")
    private Integer classType;

    @ApiModelProperty(value = "idPath")
    private String gcIdpath;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "子级分类")
    private List<GoodsClassListDTO> goodsClassListDTO;

}