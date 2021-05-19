/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.storeclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺分类表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "StoreGoodsClassDTO")
public class StoreGoodsClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String gcName;


}