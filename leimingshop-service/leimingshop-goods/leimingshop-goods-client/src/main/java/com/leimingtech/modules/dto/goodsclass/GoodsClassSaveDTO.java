/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodsclass;

import com.leimingtech.commons.tools.validator.group.AddGroup;
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
@ApiModel(description = "GoodsClassSaveDTO")
public class GoodsClassSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "分类名称不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "分类名称")
    private String gcName;

    @NotNull(message = "父ID不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "父ID")
    private Long gcParentId;

    @ApiModelProperty(value = "排序")
    private Integer gcSort;

    @ApiModelProperty(value = "层级path")
    private String gcIdpath;

    @ApiModelProperty(value = "分类类型（0:实体商品分类，1:虚拟商品分类）")
    private Integer classType;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "品牌关联对象")
    private List<GoodsClassBrandSaveDTO> goodsClassBrandSaveDTOList;

    @ApiModelProperty(value = "属性关联数组")
    private Long[] attrIds;

    @ApiModelProperty(value = "规格关联数组")
    private Long[] specIds;

}