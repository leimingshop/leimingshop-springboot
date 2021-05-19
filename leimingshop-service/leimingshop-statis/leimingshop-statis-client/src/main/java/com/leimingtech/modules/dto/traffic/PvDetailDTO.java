/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.traffic;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 浏览量保存对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(description = "PvDetailDTO")
public class PvDetailDTO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "访问IP")
    private String ipDetail;

    @ApiModelProperty(value = "浏览平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer browsePlatforms;

    @ApiModelProperty(value = "页面类型（1:首页、2:商品分类页、3:购物车、4:商品详情页、5:其他）")
    private Integer pageType;

    @ApiModelProperty("活动标记 1 优惠券 2 满减")
    private Integer activityType;

    @ApiModelProperty("满减活动Id")
    private Long activityId;

    @ApiModelProperty(value = "浏览器")
    private String browseType;

    @ApiModelProperty(value = "操作系统")
    private String operatingSystem;

    @ApiModelProperty(value = "创建时间（精确到时）")
    private Date createHourTime;

    @ApiModelProperty(value = "创建时间（精确到日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（精确到月）")
    private Date createMonthTime;

    @ApiModelProperty(value = "创建时间（精确到秒）")
    private Date createTime;

}
