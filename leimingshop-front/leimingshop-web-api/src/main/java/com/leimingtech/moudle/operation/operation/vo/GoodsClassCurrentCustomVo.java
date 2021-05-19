/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品自定义分类
 *
 * @author xuzhch
 * @date 2020年6月8日19:48:27
 */
@ApiModel(description = "GoodsClassParentCustomVo")
@Data
public class GoodsClassCurrentCustomVo implements Serializable {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long gcParentId;

    @ApiModelProperty(value = "展示类目名称")
    private String gcName;

    @ApiModelProperty(value = "关联商品分类id")
    private Long classId;

    @ApiModelProperty(value = "层级path。格式：1,2,3")
    private String idPath;

    @ApiModelProperty(value = "商品自定义分类")
    private GoodsClassCurrentCustomVo children;
}
