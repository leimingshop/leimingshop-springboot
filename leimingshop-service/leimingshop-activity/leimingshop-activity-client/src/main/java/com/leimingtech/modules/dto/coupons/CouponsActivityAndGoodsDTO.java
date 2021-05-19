/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@ApiModel(description = "CouponsActivityAndGoodsDTO")
public class CouponsActivityAndGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "优惠券活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动范围 0平台 1店铺")
    private Integer activityScope;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    @ApiModelProperty(value = "优惠券类型 0满减券 1满折券")
    private Integer couponsType;

    @ApiModelProperty(value = "活动场景 0普通券 1新人券")
    private Integer activityScene;

    @ApiModelProperty(value = "优惠券总数")
    private Long totalNum;

    @ApiModelProperty(value = "每人限领数量")
    private Long personLimit;

    @ApiModelProperty(value = "会员等级id")
    private Long memberGraderId;

    @ApiModelProperty(value = "会员等级名称")
    private String memberGraderName;

    @ApiModelProperty(value = "使用限制金额")
    private BigDecimal limitAmount;

    @ApiModelProperty(value = "优惠券面额")
    private BigDecimal faceValue;

    @ApiModelProperty(value = "领取开始时间")
    private Date getStartDate;

    @ApiModelProperty(value = "领取结束时间")
    private Date getEndDate;

    @ApiModelProperty(value = "有效期类型 0固定时间 1有效天数")
    private Integer validityType;

    @ApiModelProperty(value = "使用开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date useStartDate;

    @ApiModelProperty(value = "使用结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date useEndDate;

    @ApiModelProperty(value = "有效天数")
    private Integer validityDays;

    @ApiModelProperty(value = "优惠券剩余数量")
    private Long surplusNum;

    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    @ApiModelProperty(value = "是否与其他活动共享 0不共享 1共享")
    private Integer shareFlag;

    @ApiModelProperty(value = "审核状态 0未审核 1审核通过 2审核未通过")
    private Integer auditState;

    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;

    @ApiModelProperty(value = "活动描述")
    private String activityDescription;

    @ApiModelProperty("优惠券活动关联商品")
    private List<CouponsGoodsDTO> couponsGoodsDTOList;

}
