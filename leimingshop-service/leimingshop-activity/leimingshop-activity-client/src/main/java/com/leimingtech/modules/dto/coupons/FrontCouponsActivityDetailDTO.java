/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <front优惠券详情实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */
@Data
@ApiModel(description = "FrontCouponsActivityDetailDTO")
public class FrontCouponsActivityDetailDTO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("店铺ID")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    @ApiModelProperty("优惠券类型 0满减券 1满折券")
    private Integer couponsType;

    @ApiModelProperty("使用限制金额")
    private BigDecimal limitAmount;

    @ApiModelProperty("优惠券面额")
    private BigDecimal faceValue;

    @ApiModelProperty("有效期类型 0固定时间 1有效天数")
    private Integer validityType;

    @ApiModelProperty("使用开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date useStartDate;

    @ApiModelProperty("使用结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date useEndDate;

    @ApiModelProperty("用户使用结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty("有效天数")
    private Integer validityDays;

    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    @ApiModelProperty("是否领取 0未领取 1已领取")
    private Integer receivedFlag;

    @ApiModelProperty("活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;

}
