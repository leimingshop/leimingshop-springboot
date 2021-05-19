/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 订单地址信息实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@ApiModel(description = "OrderAddressDTO")
public class OrderAddressDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "买家ID")
    private Long buyerId;
    @ApiModelProperty(value = "会员姓名")
    private String trueName;
    @ApiModelProperty(value = "省级ID")
    private Long provinceId;
    @ApiModelProperty(value = "市级ID")
    private Long cityId;
    @ApiModelProperty(value = "地区ID")
    private Long areaId;
    @ApiModelProperty(value = "街道ID")
    private Long stressId;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "地区内容")
    private String areaInfo;
    @ApiModelProperty(value = "手机电话")
    private String mobPhone;
}