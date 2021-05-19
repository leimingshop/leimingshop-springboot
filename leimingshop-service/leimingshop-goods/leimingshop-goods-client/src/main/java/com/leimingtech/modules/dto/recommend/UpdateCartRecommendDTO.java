/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 更新购物车推荐商品表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-6
 */
@Data
@ApiModel(description = "UpdateCartRecommendDTO")
public class UpdateCartRecommendDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

}