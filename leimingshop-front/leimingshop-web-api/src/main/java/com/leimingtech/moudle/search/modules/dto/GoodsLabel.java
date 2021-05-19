/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuzhch
 * @description 商品标签
 * @date 2020年4月23日11:32:58
 */
@ApiModel(description = "GoodsLabel")
@Data
public class GoodsLabel implements Serializable {
    @ApiModelProperty(value = "商品标签ID")
    private Long labelId;
    @ApiModelProperty(value = "商品标签名称")
    private String labelName;
}
