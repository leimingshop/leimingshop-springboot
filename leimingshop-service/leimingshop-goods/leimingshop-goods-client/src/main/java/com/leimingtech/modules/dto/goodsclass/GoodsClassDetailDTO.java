/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodsclass;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * 商品分类
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */
@Data
@ApiModel(description = "GoodsClassDetailDTO")
public class GoodsClassDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "商品分类id不能为空", groups = UpdateGroup.class)
    @ApiModelProperty(value = "商品分类ID")
    private Long id;

    @NotNull(message = "分类名称不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "分类名称")
    private String gcName;

    @NotNull(message = "父ID不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "父ID")
    private Long gcParentId;

    @NotNull(message = "排序不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "排序")
    private Integer gcSort;

    @ApiModelProperty(value = "层级path")
    private String gcIdpath;

    @ApiModelProperty(value = "分类类型（0:实体商品分类，1:虚拟商品分类）")
    private Integer classType;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "品牌关联对象")
    private List<GoodsClassBrandUpdateDTO> goodsClassBrandUpdateDTO;

    @ApiModelProperty(value = "关联规格对象")
    private List<GoodsClassSpecDTO> goodsClassSpecDTO;

    @ApiModelProperty(value = "关联属性对象")
    private List<GoodsClassAttrDTO> goodsClassAttrDTO;

}