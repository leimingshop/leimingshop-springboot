/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 新增拼团数据
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(description = "InsertStoreGroupDTO")
public class InsertStoreGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "拼团名称")
    @NotBlank(message = "拼团活动名称不能为空", groups = AddGroup.class)
    @Size(message = "拼团活动名称长度不能超过20", max = 20, groups = AddGroup.class)
    private String groupName;

    @ApiModelProperty(value = "拼团开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "拼团结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "参团条件（默认0无限制，1新用户）")
    @NotNull(message = "参团条件不能为空", groups = AddGroup.class)
    @Min(value = 0, message = "有效期类型错误", groups = AddGroup.class)
    @Max(value = 1, message = "有效期类型错误", groups = AddGroup.class)
    private Integer joinFlag;

    @ApiModelProperty(value = "活动预热时间（小时）")
    @Max(value = 999, message = "活动预热时间不能超过3位数", groups = AddGroup.class)
    private Integer groupPreheat;

    @ApiModelProperty(value = "推荐拼团（0开启，1关闭）")
    private Integer recommendFlag;

    @ApiModelProperty(value = "模拟成团（0开启，1关闭）")
    private Integer simulateFlag;

    @ApiModelProperty(value = "成团有效时间（小时）")
    @NotNull(message = "成团有效时间不能为空", groups = AddGroup.class)
    private Integer validTime;

    @ApiModelProperty(value = "订单支付有效期（分钟）")
    @NotNull(message = "订单支付有效期不能为空", groups = AddGroup.class)
    private Integer payEndTime;

    @ApiModelProperty(value = "下单使用优惠（0允许，默认1不允许）")
    private Integer discountFlag;

    @ApiModelProperty(value = "下单可用抵扣（（0允许，默认1不允许））")
    private Integer deductionFlag;


}
