/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.storegoodsclass;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */
@Data
@ApiModel(value = "保存店铺商品分类表")
public class AddStoreGoodsClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @NotNull(message = "店铺商品分类名称不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "店铺商品分类名称")
    private String gcName;
    @ApiModelProperty(value = "父ID")
    private Long gcParentId;
    @ApiModelProperty(value = "展示类目图片")
    private String gcPic;
    @ApiModelProperty(value = "前台展示（0不展示，默认为1展示）")
    private Integer showFlag;
    @NotNull(message = "排序不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "排序（0-255内的整数，数值越小排序越靠前）")
    private Integer sort;
}