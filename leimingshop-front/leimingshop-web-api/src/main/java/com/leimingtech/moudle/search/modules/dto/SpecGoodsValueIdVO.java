/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 商品详情页实体
 * @Date: 2019/7/23 14:23
 * @Version: V1.0
 */
@Data
@ApiModel(description = "SpecGoodsValueIdVO")
public class SpecGoodsValueIdVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规格属性id")
    private Long specAttrId;

    @ApiModelProperty(value = "规格属性值id")
    private Long specAttrValueId;

}
