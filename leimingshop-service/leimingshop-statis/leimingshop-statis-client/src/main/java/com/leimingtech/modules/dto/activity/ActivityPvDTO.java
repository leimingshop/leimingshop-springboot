/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动浏览量保存对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "活动浏览量保存对象")
public class ActivityPvDTO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "访问IP")
    private String ipDetail;

    @ApiModelProperty("活动标记 1 优惠券 2 满减")
    private Integer activityType;

    @ApiModelProperty(value = "创建时间（精确到日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（精确到秒）")
    private Date createTime;

}
