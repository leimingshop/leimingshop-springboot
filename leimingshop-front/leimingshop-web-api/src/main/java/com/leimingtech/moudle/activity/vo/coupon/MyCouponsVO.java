/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.vo.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 我的优惠券 vo
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/16 17:33
 **/
@Data
@ApiModel(value = "我的优惠券VO")
public class MyCouponsVO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("优惠券活动id")
    private Long activityId;

    @ApiModelProperty("店铺ID")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("优惠券类型 0满减券 1满折券")
    private Integer couponsType;

    @ApiModelProperty("优惠券标识 0新到 1将过期")
    private Integer couponsFlag;

    @ApiModelProperty("使用限制金额")
    private BigDecimal limitAmount;

    @ApiModelProperty("优惠券面额")
    private BigDecimal faceValue;

    @ApiModelProperty("优惠券状态 未使用(0不可使用 1可使用) 2已使用 3已过期")
    private Integer couponsState;

    @ApiModelProperty("使用开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "使用时间")
    private Date useDate;

    @ApiModelProperty(value = "领取时间")
    private Date createDate;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "是否可凑单 1可凑单 1未到达可用时间 2非可用范围")
    private Integer addFlag;

    @ApiModelProperty("活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;
}
