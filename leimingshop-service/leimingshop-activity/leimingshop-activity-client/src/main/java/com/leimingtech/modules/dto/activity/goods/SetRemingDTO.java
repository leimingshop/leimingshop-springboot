/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <活动提醒设置实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/16
 */
@Data
@ApiModel("活动提醒设置实体")
public class SetRemingDTO {

    @ApiModelProperty(value = "秒杀场次id")
    @NotNull(message = "秒杀场次id不能为空", groups = AddGroup.class)
    private Long sessionId;

    @ApiModelProperty(value = "活动id")
    @NotNull(message = "活动id不能为空", groups = AddGroup.class)
    private Long activityId;

    @ApiModelProperty(value = "活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty(value = "商品spu id")
    @NotNull(message = "商品spu id不能为空", groups = AddGroup.class)
    private Long goodsId;

    @ApiModelProperty(value = "是否取消提醒 1 取消提醒")
    private Integer remingFlag;

}
