/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.labelrecommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签推荐关联表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Data
@ApiModel(description = "LabelRecommendRelDTO")
public class LabelRecommendRelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "标签id")
    private Long labelId;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "标签推荐排序")
    private Integer sort;
}

