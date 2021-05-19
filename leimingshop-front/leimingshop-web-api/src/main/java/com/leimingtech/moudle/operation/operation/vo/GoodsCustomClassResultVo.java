/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品分类结果
 *
 * @author xuzhch
 * @date 2020年6月8日19:49:59
 */
@ApiModel(description = "GoodsCustomClassResultVo")
@Data
public class GoodsCustomClassResultVo implements Serializable {

    @ApiModelProperty("商品展示分类")
    private GoodsClassCurrentCustomVo currentCustomVo;
    @ApiModelProperty("商品展示下级分类")
    private List<GoodsClassCustomVo> goodsClassCustomVos;
}
