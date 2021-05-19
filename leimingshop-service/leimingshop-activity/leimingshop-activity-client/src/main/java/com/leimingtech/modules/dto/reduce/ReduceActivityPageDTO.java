/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.reduce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 满减活动分页查询实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "ReduceActivityPageDTO")
public class ReduceActivityPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "满减活动名称")
    private String activityName;
    @ApiModelProperty("活动规则名称")
    private String activityRuleName;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "活动规则 0普通满减 1每满减 2阶梯满减")
    private Integer ruleType;
    @ApiModelProperty(value = "活动开始时间")
    private Date startDate;
    @ApiModelProperty(value = "活动结束时间 HH:mm:ss")
    private Date endDate;
    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;
    @ApiModelProperty(value = "使用条件金额")
    private BigDecimal limitAmount;
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal reduceAmount;
    @ApiModelProperty(value = "下单数量")
    private Integer orderNum;
    @ApiModelProperty(value = "活动内容")
    private String content;


}
