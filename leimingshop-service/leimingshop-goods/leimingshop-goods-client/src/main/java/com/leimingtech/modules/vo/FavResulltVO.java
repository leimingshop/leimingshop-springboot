/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户是否收藏店铺和商品
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@ApiModel(description = "FavResulltVO")
public class FavResulltVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("是否收藏当前商品 0 未收藏 1 已收藏")
    private Integer goodsFav;

    @ApiModelProperty("商品收藏数量")
    private Integer num;

    @ApiModelProperty("是否收藏当前店铺  0 未收藏 1 已收藏")
    private Integer storeFav;

}
