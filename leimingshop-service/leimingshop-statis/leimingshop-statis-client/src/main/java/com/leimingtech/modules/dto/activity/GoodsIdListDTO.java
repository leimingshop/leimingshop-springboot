/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品ID结果集
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "商品ID结果集")
public class GoodsIdListDTO implements Serializable {


    @ApiModelProperty(value = "商品ID")
    private Long goodsId;


}
