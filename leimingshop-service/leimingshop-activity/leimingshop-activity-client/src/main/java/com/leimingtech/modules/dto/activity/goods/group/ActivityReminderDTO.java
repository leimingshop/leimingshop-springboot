/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <活动提醒实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/24
 */
@Data
@ApiModel("活动提醒设置实体")
public class ActivityReminderDTO {

    @ApiModelProperty(value = "秒杀场次id")
    private Long sessionId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty(value = "商品spu id")
    private Long goodsId;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "0未设置 1已设置未发送 2已设置已发送")
    private Integer remindFlag;

    @ApiModelProperty(value = "提醒时间")
    private Date remindDate;

}
