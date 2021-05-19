/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodsclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品分类
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */
@Data
@ApiModel(description = "GoodsClassQueryNameDTO")
public class GoodsClassQueryNameDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String gcName;

}