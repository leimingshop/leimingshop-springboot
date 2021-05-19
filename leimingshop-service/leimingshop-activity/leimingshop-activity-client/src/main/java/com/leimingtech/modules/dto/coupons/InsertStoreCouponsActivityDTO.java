/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 新增店铺优惠券实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@ApiModel(description = "InsertStoreCouponsActivityDTO")
public class InsertStoreCouponsActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "优惠券活动名称")
    @NotBlank(message = "优惠券活动名称不能为空", groups = AddGroup.class)
    @Size(message = "优惠券活动名称长度不能超过25", max = 25, groups = AddGroup.class)
    private String activityName;

    @ApiModelProperty(value = "优惠券总数")
    @Max(message = "优惠券数量不能超过99999", value = 99999, groups = AddGroup.class)
    private Long totalNum;

    @ApiModelProperty(value = "每人限领数量")
    @Max(message = "每人限领数量不能超过99999", value = 99999, groups = AddGroup.class)
    private Long personLimit;

    @ApiModelProperty(value = "使用限制金额")
    @NotNull(message = "使用限制金额为必填项", groups = AddGroup.class)
    @DecimalMax(message = "使用限制金额不能大于99999.99", value = "99999.99", groups = AddGroup.class)
    private BigDecimal limitAmount;

    @ApiModelProperty(value = "优惠券面额")
    @NotNull(message = "优惠券面额为必填项", groups = AddGroup.class)
    @DecimalMin(message = "优惠券面额不能小于0.01", value = "0.01", groups = AddGroup.class)
    @DecimalMax(message = "优惠券面额不能大于9999.99", value = "9999.99", groups = AddGroup.class)
    private BigDecimal faceValue;

    @ApiModelProperty(value = "领取开始时间")
    @NotNull(message = "领取开始时间不能为空", groups = AddGroup.class)
    private Date getStartDate;

    @ApiModelProperty(value = "领取结束时间")
    @NotNull(message = "领取结束时间不能为空", groups = AddGroup.class)
    @Future(message = "领取结束时间不能早于当前时间", groups = AddGroup.class)
    private Date getEndDate;

    @ApiModelProperty(value = "有效期类型 0固定时间 1有效天数")
    @NotNull(message = "有效期类型不能为空", groups = AddGroup.class)
    @Min(value = 0, message = "有效期类型错误", groups = AddGroup.class)
    @Max(value = 1, message = "有效期类型错误", groups = AddGroup.class)
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
    @Max(value = 90, message = "有效天数不能超过90天", groups = AddGroup.class)
    private Integer validityDays;

    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    @NotNull(message = "活动商品范围不能为空", groups = AddGroup.class)
    private Integer activityGoodsScope;

    @ApiModelProperty("优惠券关联商品集合")
    private List<Long> relationIds;

}
