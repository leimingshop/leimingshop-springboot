/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.labelrecommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 标签推荐关联表(保存实体)
 *
 * @author weixianchun @leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Data
@ApiModel(description = "LabelRecommendRelSaveDTO")
public class LabelRecommendRelSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签id")
    private Long labelId;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "标签推荐排序")
    private Integer sort;
}
