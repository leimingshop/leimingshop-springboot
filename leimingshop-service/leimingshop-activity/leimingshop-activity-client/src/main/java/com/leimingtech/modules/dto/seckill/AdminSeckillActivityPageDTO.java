/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 后台秒杀活动分页实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(value = "后台秒杀活动分页实体")
public class AdminSeckillActivityPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "场次id")
    private Long sessionId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动开始时间")
    private Date activityStartDate;

    @ApiModelProperty(value = "活动结束时间")
    private Date activityEndDate;

    @ApiModelProperty(value = "活动有效时间")
    private Integer activityEffectiveTime;

    @ApiModelProperty(value = "浏览数")
    private Integer browseNum;

    @ApiModelProperty(value = "下单数")
    private Integer orderNum;

    @ApiModelProperty(value = "活动商品数量（件）")
    private Integer goodsNum;

    @ApiModelProperty(value = "审核状态 0未审核 1审核通过 2审核拒绝")
    private Integer auditState;

    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;
}
