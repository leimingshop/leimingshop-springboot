/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 订单地址信息实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@ApiModel(description = "UpdateOrderAddressDTO")
public class UpdateOrderAddressDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;
    @ApiModelProperty(value = "会员姓名")
    @NotBlank(message = "会员姓名不能为空", groups = UpdateGroup.class)
    private String trueName;
    @ApiModelProperty(value = "省级ID")
    @NotNull(message = "省级ID不能为空", groups = UpdateGroup.class)
    private Long provinceId;
    @ApiModelProperty(value = "市级ID")
    @NotNull(message = "市级ID不能为空", groups = UpdateGroup.class)
    private Long cityId;
    @ApiModelProperty(value = "地区ID")
    @NotNull(message = "地区ID不能为空", groups = UpdateGroup.class)
    private Long areaId;
    @ApiModelProperty(value = "街道ID")
    @NotNull(message = "街道ID不能为空", groups = UpdateGroup.class)
    private Long stressId;
    @ApiModelProperty(value = "地址")
    @NotBlank(message = "地址不能为空", groups = UpdateGroup.class)
    private String address;
    @ApiModelProperty(value = "地区内容")
    @NotBlank(message = "地区内容不能为空", groups = UpdateGroup.class)
    private String areaInfo;
    @ApiModelProperty(value = "手机电话")
    @NotBlank(message = "手机电话不能为空", groups = UpdateGroup.class)
    private String mobPhone;
    @ApiModelProperty(value = "是否默认收货地址（默认0否，1是）")
    private Integer isDefault;

}