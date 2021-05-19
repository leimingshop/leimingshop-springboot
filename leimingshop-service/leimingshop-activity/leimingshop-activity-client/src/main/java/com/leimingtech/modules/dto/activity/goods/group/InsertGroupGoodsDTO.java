/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods.group;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 新增拼团活动商品实体
 *
 * @author keyuan
 * @since v1.0.0 2020-03-11
 */
@Data
@ApiModel(value = "新增拼团活动商品实体")
public class InsertGroupGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动id")
    @NotNull(message = "活动id不能为空", groups = AddGroup.class)
    private Long activityId;

    @ApiModelProperty(value = "商品spu id")
    @NotNull(message = "商品spu id不能为空", groups = AddGroup.class)
    private Long goodsId;

    @ApiModelProperty(value = "活动商品规格集合")
    @NotEmpty(message = "活动商品规格集合不能为空", groups = AddGroup.class)
    private List<InsertGroupSkuDTO> skuList;

    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不能为空", groups = AddGroup.class)
    @Max(value = 255, message = "排序最大值为255", groups = AddGroup.class)
    private Integer sort;

    @ApiModelProperty(value = "成团人数")
    @NotNull(message = "成团人数不能为空", groups = AddGroup.class)
    @Max(value = 999, message = "成团人数不能超过3位", groups = AddGroup.class)
    private Integer regimentNum;

    @ApiModelProperty(value = "参团次数限制")
    @Max(value = 999, message = "参团次数限制不能超过3位", groups = AddGroup.class)
    @NotNull(message = "参团次数限制不能为空", groups = AddGroup.class)
    private Integer joinLimit;

    @ApiModelProperty(value = "单次购买件数")
    @NotNull(message = "单次购买件数不能为空", groups = AddGroup.class)
    @Max(value = 999, message = "单次购买件数不能超过3位", groups = AddGroup.class)
    private Integer onceBuyLimit;
}
