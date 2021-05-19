/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.storegoodsclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */
@Data
@ApiModel(value = "店铺商品分类表")
public class StoreGoodsClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺商品分类名称")
    private String gcName;
    @ApiModelProperty(value = "父ID")
    private Long gcParentId;
    @ApiModelProperty(value = "展示类目图片")
    private String gcPic;
    @ApiModelProperty(value = "前台展示（0不展示，默认为1展示）")
    private Integer showFlag;
    @ApiModelProperty(value = "排序（0-255内的整数，数值越小排序越靠前）")
    private Integer sort;
}