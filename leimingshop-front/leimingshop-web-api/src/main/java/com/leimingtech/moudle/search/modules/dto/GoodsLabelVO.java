/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述:
 * <商品标签信息>
 *
 * @author weixianchun
 * @date 2020/1/7 11:58
 **/
@Data
@ApiModel(description = "GoodsLabelVO")
public class GoodsLabelVO implements Serializable {

    @ApiModelProperty(value = "商品标签id")
    private Long labelId;
}
