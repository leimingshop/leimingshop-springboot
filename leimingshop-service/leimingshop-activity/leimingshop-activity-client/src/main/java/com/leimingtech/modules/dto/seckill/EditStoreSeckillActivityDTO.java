/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.seckill;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 编辑店铺秒杀活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(value = "编辑店铺秒杀活动实体")
public class EditStoreSeckillActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "场次id")
    @NotNull(message = "秒杀时间不能为空", groups = UpdateGroup.class)
    private Long sessionId;

    @ApiModelProperty(value = "活动名称")
    @NotBlank(message = "活动名称不能为空", groups = UpdateGroup.class)
    private String activityName;

    @ApiModelProperty(value = "秒杀活动有效期")
    @NotNull(message = "秒杀活动有效期不能为空", groups = UpdateGroup.class)
    @Min(value = 1, message = "秒杀活动有效期不小于1小时", groups = UpdateGroup.class)
    @Max(value = 48, message = "秒杀活动有效期不大于48小时", groups = UpdateGroup.class)
    private Integer activityEffectiveTime;

    @ApiModelProperty(value = "会员等级id")
    private Long memberGradeId;

    @ApiModelProperty(value = "活动限购数量")
    @Max(value = 9999999, message = "活动限购数量最大值为9999999", groups = UpdateGroup.class)
    private Integer restrictionQuantity;

    @ApiModelProperty(value = "积分是否可用 0不可用 1可用")
    private Integer pointFlag;

    @ApiModelProperty(value = "余额是否可用 0不可用 1可用")
    private Integer balanceFlag;

    @ApiModelProperty(value = "优惠券是否可用 0不可用 1可用")
    private Integer couponsFlag;

    @ApiModelProperty(value = "满减是否可用 0不可用 1可用")
    private Integer reduceFlag;

    @ApiModelProperty(value = "满包邮是否可用 0不可用 1可用")
    private Integer freeShippingFlag;
}
