/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动定时统计
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "活动定时统计")
public class ActivityStatisEntity implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty("活动标记 1 优惠券 2 满减")
    private Integer activityType;

    @ApiModelProperty("名称")
    private String activityName;

    @ApiModelProperty("领取量")
    private Integer personNum;

    @ApiModelProperty("下单订单数量")
    private Integer downOrderNum;

    @ApiModelProperty("下单使用数量")
    private Integer userOrderNum;

    @ApiModelProperty("下单金额")
    private BigDecimal orderAmount = BigDecimal.ZERO;

    @ApiModelProperty("支付订单数")
    private Integer payOrderNum;

    @ApiModelProperty("支付使用量")
    private Integer payUseNum;

    @ApiModelProperty("下单人数")
    private Integer downOrderPerson;

    @ApiModelProperty("支付人数")
    private Integer payPerson;

    @ApiModelProperty("应收金额")
    private BigDecimal realityAmount = BigDecimal.ZERO;

    @ApiModelProperty("实付金额")
    private BigDecimal payAmount = BigDecimal.ZERO;

    @ApiModelProperty("活动减免金额")
    private BigDecimal activityReduceAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "创建时间（精确到日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（精确到秒）")
    private Date createTime;


    public void setOrderAmount(BigDecimal orderAmount) {

        if (orderAmount == null) {
            this.orderAmount = BigDecimal.ZERO;
        } else {
            this.orderAmount = orderAmount;
        }
    }

    public void setRealityAmount(BigDecimal realityAmount) {
        if (realityAmount == null) {
            this.realityAmount = BigDecimal.ZERO;
        } else {
            this.realityAmount = realityAmount;
        }
    }

    public void setPayAmount(BigDecimal payAmount) {
        if (payAmount == null) {
            this.payAmount = BigDecimal.ZERO;
        } else {
            this.payAmount = payAmount;
        }
    }

    public void setActivityReduceAmount(BigDecimal activityReduceAmount) {
        if (activityReduceAmount == null) {
            this.activityReduceAmount = BigDecimal.ZERO;
        } else {
            this.activityReduceAmount = activityReduceAmount;
        }
    }

    public void setPersonNum(Integer personNum) {
        if (personNum == null) {
            this.personNum = 0;
        } else {
            this.personNum = personNum;
        }

    }

    public void setDownOrderNum(Integer downOrderNum) {
        if (downOrderNum == null) {
            this.downOrderNum = 0;
        } else {
            this.downOrderNum = downOrderNum;
        }
    }

    public void setUserOrderNum(Integer userOrderNum) {
        if (userOrderNum == null) {
            this.userOrderNum = 0;
        } else {
            this.userOrderNum = userOrderNum;
        }
    }

    public void setPayOrderNum(Integer payOrderNum) {
        if (payOrderNum == null) {
            this.payOrderNum = 0;
        } else {
            this.payOrderNum = payOrderNum;
        }
    }

    public void setPayUseNum(Integer payUseNum) {
        if (payUseNum == null) {
            this.payUseNum = 0;
        } else {
            this.payUseNum = payUseNum;
        }
    }

    public void setDownOrderPerson(Integer downOrderPerson) {
        if (downOrderPerson == null) {
            this.downOrderPerson = 0;
        } else {
            this.downOrderPerson = downOrderPerson;
        }
    }

    public void setPayPerson(Integer payPerson) {
        if (payPerson == null) {
            this.payPerson = 0;
        } else {
            this.payPerson = payPerson;
        }
    }
}
