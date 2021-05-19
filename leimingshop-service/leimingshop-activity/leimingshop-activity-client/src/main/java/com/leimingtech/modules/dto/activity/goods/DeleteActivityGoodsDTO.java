/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <活动商品删除实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/11
 */
@Data
@ApiModel("活动商品删除实体")
public class DeleteActivityGoodsDTO {

    @ApiModelProperty(value = "活动id")
    @NotNull(message = "活动id不能为空", groups = DefaultGroup.class)
    private Long activityId;

    @ApiModelProperty(value = "商品spu id")
    @NotEmpty(message = "商品spu id不能为空", groups = DefaultGroup.class)
    private List<Long> goodsIds;

}
