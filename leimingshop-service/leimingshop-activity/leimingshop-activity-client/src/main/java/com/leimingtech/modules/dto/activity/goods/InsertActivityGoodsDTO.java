/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 新增秒杀活动商品实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(value = "新增秒杀活动商品实体")
public class InsertActivityGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动id")
    @NotNull(message = "活动id不能为空", groups = AddGroup.class)
    private Long activityId;

    @ApiModelProperty(value = "商品spu id")
    @NotNull(message = "商品spu id不能为空", groups = AddGroup.class)
    private Long goodsId;

    @ApiModelProperty(value = "活动商品规格集合")
    @NotEmpty(message = "活动商品规格集合不能为空", groups = AddGroup.class)
    private List<InsertSeckillSkuDTO> skuList;

}
