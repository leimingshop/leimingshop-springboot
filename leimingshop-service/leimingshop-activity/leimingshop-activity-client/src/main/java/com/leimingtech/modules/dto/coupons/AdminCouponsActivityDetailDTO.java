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
 * 后台优惠券活动详情实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@ApiModel(description = "AdminCouponsActivityDetailDTO")
public class AdminCouponsActivityDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "优惠券活动名称")
    private String activityName;

    @ApiModelProperty(value = "优惠券类型 0满减券 1满折券")
    private Integer couponsType;

    @ApiModelProperty(value = "每人限领数量")
    private Long personLimit;

    @ApiModelProperty(value = "使用数量")
    private Integer usedNum;

    @ApiModelProperty(value = "发券数量")
    private Integer receivedNum;

    @ApiModelProperty(value = "优惠券总数")
    private Long totalNum;

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

    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;

    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    @ApiModelProperty(value = "")
    private List<CouponsGoodsDTO> couponsGoodsDTOList;

}
