/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询营销统计
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "查询营销统计")
public class ActivityStatisDTO implements Serializable {
    public static final Integer INIT_DATA_INTEGER = 0;
    public static final Double INIT_DATA_DOUBLE = 0.0;
    @ApiModelProperty(value = "浏览量")
    private Integer pageView;
    @ApiModelProperty(value = "访客量")
    private Integer visitorsNumber;
    @ApiModelProperty("领取量")
    private Integer personNum;
    @ApiModelProperty("活动标记 1 优惠券 2 满减")
    private Integer activityType;
    @ApiModelProperty("下单订单数量")
    private Integer downOrderNum;
    @ApiModelProperty("下单人数")
    private Integer downOrderPerson;
    @ApiModelProperty("下单使用数量")
    private Integer userOrderNum;
    @ApiModelProperty("下单金额")
    private BigDecimal orderAmount;
    @ApiModelProperty("支付订单数")
    private Integer payOrderNum;
    @ApiModelProperty("支付使用量")
    private Integer payUseNum;
    @ApiModelProperty("支付人数")
    private Integer payPerson;
    @ApiModelProperty("应收金额")
    private BigDecimal realityAmount;
    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;
    @ApiModelProperty("活动减免金额")
    private BigDecimal activityReduceAmount;
    @ApiModelProperty("转化率")
    private Double conversion;
    @ApiModelProperty(value = "创建时间（精确到日）")
    private Date createDayTime;
    @ApiModelProperty(value = "创建时间（精确到秒）")
    private Date createTime;

    public static ActivityStatisDTO newActivityStatisDTO() {
        ActivityStatisDTO activityStatisDTO = new ActivityStatisDTO();
        activityStatisDTO.setConversion(INIT_DATA_DOUBLE);
        activityStatisDTO.setActivityReduceAmount(BigDecimal.ZERO);
        activityStatisDTO.setPayAmount(BigDecimal.ZERO);
        activityStatisDTO.setRealityAmount(BigDecimal.ZERO);
        activityStatisDTO.setOrderAmount(BigDecimal.ZERO);
        activityStatisDTO.setPayPerson(INIT_DATA_INTEGER);
        activityStatisDTO.setPayUseNum(INIT_DATA_INTEGER);
        activityStatisDTO.setPayOrderNum(INIT_DATA_INTEGER);
        activityStatisDTO.setPageView(INIT_DATA_INTEGER);
        activityStatisDTO.setVisitorsNumber(INIT_DATA_INTEGER);
        activityStatisDTO.setPersonNum(INIT_DATA_INTEGER);
        activityStatisDTO.setActivityType(INIT_DATA_INTEGER);
        activityStatisDTO.setDownOrderNum(INIT_DATA_INTEGER);
        activityStatisDTO.setDownOrderPerson(INIT_DATA_INTEGER);
        activityStatisDTO.setUserOrderNum(INIT_DATA_INTEGER);
        return activityStatisDTO;
    }

}
