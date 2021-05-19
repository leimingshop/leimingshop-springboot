/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.flashsale;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 限时抢购活动修改信息
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */
@Data
@ApiModel(description = "FlashSaleActivitySaveDTO")
public class FlashSaleActivityEditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @NotNull(message = "活动ID不能为空", groups = UpdateGroup.class)
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    @NotNull(message = "店铺ID不能为空", groups = UpdateGroup.class)
    private Long storeId;
    @ApiModelProperty(value = "活动名称")
    @NotBlank(message = "活动名称不能为空", groups = UpdateGroup.class)
    private String activityName;
    @ApiModelProperty(value = "活动开始时间")
    @NotNull(message = "活动开始时间不能为空", groups = UpdateGroup.class)
    private Date activityStartDate;
    @ApiModelProperty(value = "活动结束时间")
    @NotNull(message = "活动结束时间不能为空", groups = UpdateGroup.class)
    private Date activityEndDate;
    @ApiModelProperty(value = "活动状态(默认:0未开始，1进行中，2已结束)")
    private Integer activityState;
    @ApiModelProperty(value = "订单支付有效时间(单位：分钟)")
    @NotNull(message = "订单支付有效时间不能为空", groups = UpdateGroup.class)
    @Min(value = 1, message = "订单支付有效时间不能小于1分钟", groups = UpdateGroup.class)
    @Max(value = 1440, message = "订单支付有效时间不能大于1440分钟", groups = UpdateGroup.class)
    private Integer payValidTime;
    @ApiModelProperty(value = "会员等级id")
    private Long memberGradeId;
    @ApiModelProperty(value = "会员等级名称")
    private String memberGradeName;
    @ApiModelProperty(value = "会员等级积分")
    private Integer memberGradeLimit;
    @ApiModelProperty(value = "活动限购数量")
    @Max(value = 99999, message = "活动限购数量不能大于99999", groups = UpdateGroup.class)
    private Integer restrictionQuantity;
    @ApiModelProperty(value = "积分是否可用(默认：0不可用，1可用)")
    @NotNull(message = "积分是否可用不能为空", groups = UpdateGroup.class)
    private Integer pointFlag;
    @ApiModelProperty(value = "余额是否可用(默认：0不可用，1可用)")
    @NotNull(message = "余额是否可用不能为空", groups = UpdateGroup.class)
    private Integer balanceFlag;
    @ApiModelProperty(value = "优惠券是否可用 (默认：0不可用，1可用)")
    @NotNull(message = "优惠券是否可用不能为空", groups = UpdateGroup.class)
    private Integer couponsFlag;
    @ApiModelProperty(value = "满减是否可用(默认：0不可用，1可用)")
    @NotNull(message = "满减是否可用不能为空", groups = UpdateGroup.class)
    private Integer reduceFlag;
}
