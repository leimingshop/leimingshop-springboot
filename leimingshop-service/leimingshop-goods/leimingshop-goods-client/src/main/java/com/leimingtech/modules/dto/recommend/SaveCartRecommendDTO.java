/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 购物车推荐商品表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-6
 */
@Data
@ApiModel(description = "SaveCartRecommendDTO")
public class SaveCartRecommendDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "商品spu的id")
    private Long goodsId;
    @ApiModelProperty(value = "所属分类")
    private String category;

}